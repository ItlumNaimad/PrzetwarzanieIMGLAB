import org.opencv.core.*;
import org.opencv.highgui.HighGui;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Lab03 {
    // METODA SATURATE DLA ZADANIE 1 WZIĘTA Z DOKUMENTACJI OPENCV
    private byte saturate(double val) {
        int iVal = (int) Math.round(val);
        iVal = iVal > 255 ? 255 : (iVal < 0 ? 0 : iVal);
        return (byte) iVal;
    }
    // METODA CONTRAST WZIĘTA Z DOKUMENTACJI OPENCV
    private Mat contrast(Mat base_image) {
        Mat newImage = Mat.zeros(base_image.size(), base_image.type());
        double alpha = 1.0; // Kontrast
        int beta = 0;       // Jasność
        System.out.println(" Basic Linear Transforms ");
        System.out.println("-------------------------");
        try (Scanner scanner = new Scanner(System.in)) {
            System.out.print("* Wprowadź wartość alfa odpowiedzialną za kontrast [1,0-3,0]: ");
            alpha = scanner.nextDouble();
            System.out.print("* Wprowadź wartośc beta odpowiedzialną za jasność [0-100]: ");
            beta = scanner.nextInt();
        }
        byte[] imageData = new byte[(int) (base_image.total()*base_image.channels())];
        base_image.get(0, 0, imageData);
        byte[] newImageData = new byte[(int) (newImage.total()*newImage.channels())];
        for (int y = 0; y < base_image.rows(); y++) {
            for (int x = 0; x < base_image.cols(); x++) {
                for (int c = 0; c < base_image.channels(); c++) {
                    double pixelValue = imageData[(y * base_image.cols() + x) * base_image.channels() + c];
                    pixelValue = pixelValue < 0 ? pixelValue + 256 : pixelValue;
                    newImageData[(y * base_image.cols() + x) * base_image.channels() + c]
                            = saturate(alpha * pixelValue + beta);
                }
            }
        }
        newImage.put(0, 0, newImageData);
        return newImage;
    }
    //ZAD 4
    private Mat normalize(Mat base_image) {
        // Znajdywanie wartości maksymalną i minimalną przed normalizacją
        Core.MinMaxLocResult minMaxBefore = Core.minMaxLoc(base_image);
        System.out.println("Przed normalizacją:");
        System.out.println("Minimalna wartość piksela: " + minMaxBefore.minVal);
        System.out.println("Maksymalna wartość piksela: " + minMaxBefore.maxVal);

        // Normalizacja obrazu w przedziale <0, 255>
        Mat normalizedImage = new Mat();
        Core.normalize(base_image, normalizedImage, 0, 255, Core.NORM_MINMAX, CvType.CV_8U);

        // Znajdywanie wartości maksymalną i minimalną po normalizacji
        Core.MinMaxLocResult minMaxAfter = Core.minMaxLoc(normalizedImage);
        System.out.println("Po normalizacji:");
        System.out.println("Minimalna wartość piksela: " + minMaxAfter.minVal);
        System.out.println("Maksymalna wartość piksela: " + minMaxAfter.maxVal);

        return normalizedImage;
    }
    //ZAD 5
    public void rgbChannels(Mat base_image) {
        // Rozdziel obraz na kanały
        List<Mat> channels = new ArrayList<>();
        // Rozdzielenie obrazu na trzy kanałay (parametr channels)
        Core.split(base_image, channels);

        // Macierze na obrazy dla poszczególnych kanałów
        Mat blueChannel = new Mat();
        Mat greenChannel = new Mat();
        Mat redChannel = new Mat();

        /*
         Wyzeruj pozostałe kanały, aby widoczny był tylko dany kanał
         BGR channels.get() zachowuje kanał, a Mat.zeros() przypisuje zera w macierzy
         Dlatego zostawiamy kanał, dla którego koloru chcemy zostawić, a reszte kanałów zerujemy
        */
        List<Mat> blueOnly = new ArrayList<>(List.of(channels.get(0), Mat.zeros(base_image.size(), CvType.CV_8U), Mat.zeros(base_image.size(), CvType.CV_8U)));
        List<Mat> greenOnly = new ArrayList<>(List.of(Mat.zeros(base_image.size(), CvType.CV_8U), channels.get(1), Mat.zeros(base_image.size(), CvType.CV_8U)));
        List<Mat> redOnly = new ArrayList<>(List.of(Mat.zeros(base_image.size(), CvType.CV_8U), Mat.zeros(base_image.size(), CvType.CV_8U), channels.get(2)));

        // Tutaj łączymy kanałay z przygotowanymi wcześniej macierzami
        Core.merge(blueOnly, blueChannel);
        Core.merge(greenOnly, greenChannel);
        Core.merge(redOnly, redChannel);

        // Zapisz każdy kanał jako osobny obraz na dysku
        Imgcodecs.imwrite("kanał_niebieski.jpg", blueChannel);
        Imgcodecs.imwrite("kanał_zielony.jpg", greenChannel);
        Imgcodecs.imwrite("kanał_czerwony.jpg", redChannel);

        HighGui.imshow("Niebieski", blueChannel);
        HighGui.imshow("Zielony", greenChannel);
        HighGui.imshow("Czerwony", redChannel);
        HighGui.waitKey();
    }
    // ZADANIE 6
    public Mat convertToHSV(Mat image) {
        Mat hsvImage = new Mat();
        Imgproc.cvtColor(image, hsvImage, Imgproc.COLOR_BGR2HSV);
        return hsvImage;
    }
    // ZADANIE 7
    public void applyThresholds(Mat image) {
        // Konwertuj obraz do skali szarości
        Mat grayImage = new Mat();
        Imgproc.cvtColor(image, grayImage, Imgproc.COLOR_BGR2GRAY);

        // Różne metody binaryzacji
        Mat binary = new Mat();
        Mat binaryInv = new Mat();
        Mat truncate = new Mat();
        Mat toZero = new Mat();
        Mat toZeroInv = new Mat();

        // Próg binaryzacji
        double thresholdValue = 50;

        // Przeprowadź binaryzację z różnymi metodami
        Imgproc.threshold(grayImage, binary, thresholdValue, 200, Imgproc.THRESH_BINARY);
        Imgproc.threshold(grayImage, binaryInv, thresholdValue, 200, Imgproc.THRESH_BINARY_INV);
        Imgproc.threshold(grayImage, truncate, thresholdValue, 200, Imgproc.THRESH_TRUNC);
        Imgproc.threshold(grayImage, toZero, thresholdValue, 200, Imgproc.THRESH_TOZERO);
        Imgproc.threshold(grayImage, toZeroInv, thresholdValue, 200, Imgproc.THRESH_TOZERO_INV);

        // Wyświetl wyniki
        HighGui.imshow("Oryginalny obraz", image);
        HighGui.imshow("Binary Threshold", binary);
        HighGui.imshow("Binary Threshold Inverted", binaryInv);
        HighGui.imshow("Truncate Threshold", truncate);
        HighGui.imshow("To Zero Threshold", toZero);
        HighGui.imshow("To Zero Threshold Inverted", toZeroInv);
        HighGui.waitKey();
    }
    // ZADANIE 8
    public void matrixOperations(Mat imageA, Mat imageB) {
        // Sprawdzenie, czy obrazy są tego samego rozmiaru
        // Dodałem je w razie gdyby była potrzeba zmiany obrazów na inne
        if (imageA.size().equals(imageB.size()) && imageA.type() == imageB.type()) {
            Mat addResult = new Mat();
            Mat subResultAB = new Mat();
            Mat subResultBA = new Mat();
            Mat mulResult = new Mat();
            Mat divResultAB = new Mat();
            Mat divResultBA = new Mat();

            // Odejmowanie A + B
            Core.add(imageA, imageB, addResult);
            Imgcodecs.imwrite("result_add.jpg", addResult);

            // Odejmowanie A - B
            Core.subtract(imageA, imageB, subResultAB);
            Imgcodecs.imwrite("result_subtract_AB.jpg", subResultAB);

            // Odejmowanie B - A
            Core.subtract(imageB, imageA, subResultBA);
            Imgcodecs.imwrite("result_subtract_BA.jpg", subResultBA);

            // Mnożenie A * B
            Core.multiply(imageA, imageB, mulResult);
            Imgcodecs.imwrite("result_multiply.jpg", mulResult);

            // Dzielenie A / B (dzielenie przez zero wstawia czarne piksele)
            Core.divide(imageA, imageB, divResultAB);
            Imgcodecs.imwrite("result_divide_AB.jpg", divResultAB);

            // Dzielenie B / A (ta sama zasada co wyżej)
            Core.divide(imageB, imageA, divResultBA);
            Imgcodecs.imwrite("result_divide_BA.jpg", divResultBA);

            System.out.println("Operacje arytmetyczne zakończone i zapisane na dysku.");
        } else {
            System.out.println("Obrazy muszą mieć ten sam rozmiar i typ, aby można było wykonać operacje arytmetyczne.");
        }
    }
    // ZADANIE 9
    public void histogram(Mat image) {
        // Rozdzielanie obrazu na kanały B, G, R
        List<Mat> channels = new ArrayList<>();
        Core.split(image, channels);
        // Parametry histogramu
        MatOfInt histSize = new MatOfInt(256); // Liczba "kubełków" histogramu
        MatOfFloat histRange = new MatOfFloat(0, 256); // Zakres wartości pikseli
        // Kolory histogramu dla każdego kanału
        Scalar[] colors = { new Scalar(255, 0, 0), new Scalar(0, 255, 0), new Scalar(0, 0, 255) };
        String[] channelNames = { "Blue", "Green", "Red" };
        // Utworzenie macierzy na histogramy dla każdego kanału
        List<Mat> histograms = new ArrayList<>();
        // Obliczenie histogramu dla każdego kanału
        for (Mat channel : channels) {
            Mat hist = new Mat();
            Imgproc.calcHist(List.of(channel), new MatOfInt(0), new Mat(), hist, histSize, histRange);
            histograms.add(hist);
        }
        // Rysowanie histogramu
        int histWidth = 512;
        int histHeight = 400;
        int binWidth = (int) Math.round((float) histWidth / histSize.get(0, 0)[0]);

        Mat histImage = new Mat(histHeight, histWidth, image.type(), new Scalar(0, 0, 0));

        for (int i = 0; i < histograms.size(); i++) {
            // Normalizacja histogramu
            Core.normalize(histograms.get(i), histograms.get(i), 0, histImage.rows(), Core.NORM_MINMAX);

            // Rysowanie linii dla każdego kanału
            for (int j = 1; j < histSize.get(0, 0)[0]; j++) {
                int x1 = binWidth * (j - 1);
                int y1 = (int) (histHeight - Math.round(histograms.get(i).get(j - 1, 0)[0]));
                int x2 = binWidth * j;
                int y2 = (int) (histHeight - Math.round(histograms.get(i).get(j, 0)[0]));

                Imgproc.line(histImage, new org.opencv.core.Point(x1, y1), new org.opencv.core.Point(x2, y2), colors[i], 2);
            }
        }
        // Wyświetlenie histogramu
        HighGui.imshow("Histogram", histImage);
        HighGui.waitKey();
    }

    public Lab03()
    {
        Mat image = Imgcodecs.imread("zapisane.jpg");
        if (image.empty()) {
            System.out.println("Empty image: " + "zapisane.jpg");
            System.exit(0);
        }
        //ZAD5
        //Mat rgb = Imgcodecs.imread("kanalyrgp.JPG");
        //rgbChannels(rgb);
        //ZAD4
        // Wywołanie funkcji contrast która zwraca macierz ze zmianą kontrastu
        // Mat newImage = contrast(image);
        // Wywołanie funkcji normalize, która normalizuje obraz
        //Mat grayImage = new Mat();
        //Imgproc.cvtColor(image, grayImage, Imgproc.COLOR_BGR2GRAY);
        //Mat newImage = normalize(grayImage);
        // ZAD 6
        //Mat newImage = convertToHSV(image);
        // ZAD 7
        //applyThresholds(image);
        // ZAD 8
        //matrixOperations(image, image);
        //ZAD 9
        histogram(image);
        HighGui.imshow("Original Image", image);
        //HighGui.imshow("New Image", newImage);
        HighGui.waitKey();
        System.exit(0);
    }

}
