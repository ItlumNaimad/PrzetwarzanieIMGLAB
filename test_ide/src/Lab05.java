import org.opencv.core.*;
import org.opencv.highgui.HighGui;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import java.util.Random;

import static org.opencv.imgproc.Imgproc.COLOR_BGR2GRAY;
import static org.opencv.imgproc.Imgproc.cvtColor;

public class Lab05 {
    static {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    }
    /**
     * Funkcja do wczytywania obrazów, bo we wcześniejszych
     * *projektach było to uciążające co chwilę zmieniać macierz
     **/
    private Mat loadImage(String filePath) {
        Mat image = Imgcodecs.imread(filePath);
        if (image.empty()) {
            System.out.println("Nie udało się wczytać obrazu: " + filePath);
            System.exit(0);
        }
        return image;
    }
    //ZAdanie 1
    private void gaussianBlur(Mat image) {
        Mat blurredImage1 = new Mat();
        Mat blurredImage2 = new Mat();
        Mat blurredImage3 = new Mat();

        // Rozmycie z różnymi maskami
        Imgproc.GaussianBlur(image, blurredImage1, new Size(7, 7), 0);
        Imgproc.GaussianBlur(image, blurredImage2, new Size(15, 15), 0);
        Imgproc.GaussianBlur(image, blurredImage3, new Size(25, 25), 0);

        // Wyświetl wyniki
        HighGui.imshow("Oryginalny obraz", image);
        HighGui.imshow("Rozmycie Gaussa 7x7", blurredImage1);
        HighGui.imshow("Rozmycie Gaussa 15x15", blurredImage2);
        HighGui.imshow("Rozmycie Gaussa 25x25", blurredImage3);
        HighGui.waitKey();
        System.exit(0);
    }

    // Zadanie 2
    private void medianBlur(Mat image) {
        Mat medianBlur5x5 = new Mat();
        Mat medianBlur7x7 = new Mat();
        Mat medianBlur15x15 = new Mat();

        // Rozmycie medianowe z różnymi rozmiarami maski
        Imgproc.medianBlur(image, medianBlur5x5, 5); // Rozmiar maski: 3x3
        Imgproc.medianBlur(image, medianBlur7x7, 7); // Rozmiar maski: 5x5
        Imgproc.medianBlur(image, medianBlur15x15, 15); // Rozmiar maski: 7x7

        // Wyświetl wyniki
        HighGui.imshow("Oryginalny obraz", image);
        HighGui.imshow("Rozmycie medianowe 5x5", medianBlur5x5);
        HighGui.imshow("Rozmycie medianowe 7x7", medianBlur7x7);
        HighGui.imshow("Rozmycie medianowe 15x15", medianBlur15x15);

        // Poczekaj na klawisz, a potem zamknij program
        HighGui.waitKey();
        System.exit(0);
    }

    // Zadanie 3
    private void bilateralFilter(Mat image) {
        Mat bilateralBlur1 = new Mat();
        Mat bilateralBlur2 = new Mat();
        Mat bilateralBlur3 = new Mat();

        // Rozmycie bilateralne z różnymi parametrami
        Imgproc.bilateralFilter(image, bilateralBlur1, 9, 75, 75); // Wielkość sąsiedztwa 9, Sigma kolor i przestrzeń 75
        Imgproc.bilateralFilter(image, bilateralBlur2, 15, 150, 150); // Wielkość sąsiedztwa 15, Sigma kolor i przestrzeń 150
        Imgproc.bilateralFilter(image, bilateralBlur3, 25, 300, 300); // Wielkość sąsiedztwa 25, Sigma kolor i przestrzeń 300

        // Wyświetlenie wyników i zakończenie programu
        HighGui.imshow("Oryginalny obraz", image);
        HighGui.imshow("Rozmycie bilateralne (9, 75, 75)", bilateralBlur1);
        HighGui.imshow("Rozmycie bilateralne (15, 150, 150)", bilateralBlur2);
        HighGui.imshow("Rozmycie bilateralne (25, 300, 300)", bilateralBlur3);

        HighGui.waitKey();
        System.exit(0);
    }
// Zadanie 4
    private void starFilter(Mat image, float strength) {
        // Definicja filtra gwiazdkowego (kernel)
        Mat kernel = new Mat(5, 5, CvType.CV_32F) {
            {
                put(0, 0, 4);  put(0, 1, 0);  put(0, 2, 4);  put(0, 3, 0);  put(0, 4, 4);
                put(1, 0, 0);  put(1, 1, 6);  put(1, 2, 6);  put(1, 3, 6);  put(1, 4, 0);
                put(2, 0, 0);  put(2, 1, 4);  put(2, 2, 12); put(2, 3, 4);  put(2, 4, 0);
                put(3, 0, 0);  put(3, 1, 6);  put(3, 2, 4);  put(3, 3, 6);  put(3, 4, 0);
                put(4, 0, 4);  put(4, 1, 0);  put(4, 2, 0);  put(4, 3, 0);  put(4, 4, 4);
            }
        };
        Core.normalize(kernel, kernel, 0, strength, Core.NORM_MINMAX);
        // Macierz na wynikowy obraz
        Mat starFilteredImage = new Mat();
        // Zastosowanie filtra z ekstrapolacją krawędzi BORDER_REFLECT_101
        Imgproc.filter2D(image, starFilteredImage, -1, kernel, new Point(-1, -1), 0, Core.BORDER_REFLECT_101);

        // Wyświetlenie wyników
        HighGui.imshow("Oryginalny obraz", image);
        HighGui.imshow("Obraz po zastosowaniu filtra gwiazdkowego", starFilteredImage);

        // Poczekaj na klawisz, a potem zamknij program
        HighGui.waitKey();
        System.exit(0);
    }
// Zadanie 5
    private void sharpenImage(Mat image) {
        // 1. Wyostrzanie metodą konwolucji z użyciem jądra wyostrzającego
        Mat kernelSharpen = new Mat(3, 3, CvType.CV_32F) {
            {
                put(0, 0,  0); put(0, 1, -1); put(0, 2,  0);
                put(1, 0, -1); put(1, 1,  5); put(1, 2, -1);
                put(2, 0,  0); put(2, 1, -1); put(2, 2,  0);
            }
        };
        Mat convolvedImage = new Mat();
        Imgproc.filter2D(image, convolvedImage, -1, kernelSharpen);

        // 2. Wyostrzanie z użyciem Laplasjanu
        Mat laplacianImage = new Mat();
        Imgproc.Laplacian(image, laplacianImage, CvType.CV_8U);
        Mat laplacianSharpened = new Mat();
        Core.addWeighted(image, 1.5, laplacianImage, -0.5, 0, laplacianSharpened); // Połączenie obrazu i Laplasjanu

        // 3. Wyostrzanie za pomocą operacji morfologicznych
        Mat dilated = new Mat();
        Mat eroded = new Mat();
        Mat morphSharpened = new Mat();
        Imgproc.dilate(image, dilated, Mat.ones(new Size(3, 3), CvType.CV_8U));
        Imgproc.erode(image, eroded, Mat.ones(new Size(3, 3), CvType.CV_8U));
        Core.subtract(dilated, eroded, morphSharpened); // Wyostrzenie przez różnicę dylatacji i erozji
        Core.addWeighted(image, 1.0, morphSharpened, 1.0, 0, morphSharpened);

        HighGui.imshow("Oryginalny obraz", image);
        HighGui.imshow("Wyostrzanie konwolucją", convolvedImage);
        HighGui.imshow("Wyostrzanie Laplasjanem", laplacianSharpened);
        HighGui.imshow("Wyostrzanie morfologiczne", morphSharpened);

        HighGui.waitKey();
        System.exit(0);
    }
    // Zadanie 6
    private Mat addSaltAndPepperNoise(Mat image, double noiseRatio) {
        Mat noisyImage = image.clone();
        Random random = new Random();
        int totalPixels = (int) (image.rows() * image.cols() * noiseRatio);

        for (int i = 0; i < totalPixels; i++) {
            int row = random.nextInt(image.rows());
            int col = random.nextInt(image.cols());
            int channel = random.nextInt(image.channels());

            // Decyduj, czy piksel będzie czarny (0) czy biały (255)
            double value = random.nextBoolean() ? 0 : 255;
            double[] pixel = noisyImage.get(row, col);
            pixel[channel] = value; // Modyfikuj tylko jeden kanał
            noisyImage.put(row, col, pixel);
        }
        return noisyImage;
    }
    //Zadanie 7
    private Mat addGaussianNoise(Mat image, double mean, double stdDev) {
        /**
         * Funkcja generuje macierz losowych wartości z
         * rozkładu normalnego o zadanej średniej (mean) i odchyleniu standardowym (stdDev).
         **/
        Mat noisyImage = new Mat(image.size(), image.type());
        Mat gaussianNoise = new Mat(image.size(), image.type());
        Core.randn(gaussianNoise, mean, stdDev); // Generowanie losowych wartości Gaussa
        Core.add(image, gaussianNoise, noisyImage); // Dodanie szumu do oryginalnego obrazu
        return noisyImage;
    }
    public Lab05(){
        Mat image = loadImage("zapisane.jpg");
        //gaussianBlur(image);
        //medianBlur(image);
        //bilateralFilter(image);
        //starFilter(image, 0.35f);
        //sharpenImage(image);

        // ZADANIE 6
        //Mat imagePepperSalt = addSaltAndPepperNoise(image, 0.10);
        //gaussianBlur(imagePepperSalt);
        //medianBlur(imagePepperSalt);
        //bilateralFilter(imagePepperSalt);

        // ZADANIE 7
        Mat gausseNoisImg = addGaussianNoise(image,25,10);
        //gaussianBlur(gausseNoisImg);
        //medianBlur(gausseNoisImg);
        bilateralFilter(gausseNoisImg);
        }
}
