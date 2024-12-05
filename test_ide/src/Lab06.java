import org.opencv.core.*;
import org.opencv.highgui.HighGui;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import java.util.ArrayList;
import java.util.List;

public class Lab06 {
    static{
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    }
    private Mat loadImage(String filePath) {
        Mat image = Imgcodecs.imread(filePath);
        if (image.empty()) {
            System.out.println("Nie udało się wczytać obrazu: " + filePath);
            System.exit(0);
        }
        return image;
    }

    // *** ZADANIE 1 ***
    private void CannyEdges(Mat image) {
        /*
            Najpierw tworzę nową macierz i przetwarzam macierz z obrazem z parametru
            na obrac w skali szarości. Następnie tworzone są 3 macierze na każde
            użycie Canny
         */
        Mat grayImage = new Mat();
        Imgproc.cvtColor(image, grayImage, Imgproc.COLOR_BGR2GRAY);

        // Wykrywanie krawędzi z różnymi parametrami
        Mat edgesLowThreshold = new Mat();
        Mat edgesHighThreshold = new Mat();
        Mat edgesBalanced = new Mat();

        // Parametry metody Canny
        Imgproc.Canny(grayImage, edgesLowThreshold, 15, 50); // Niski próg
        Imgproc.Canny(grayImage, edgesBalanced, 40, 80);    // Zrównoważony próg
        Imgproc.Canny(grayImage, edgesHighThreshold, 70, 110); // Wysoki próg

        // Wyświetlenie wyników
        HighGui.imshow("Oryginalny obraz", image);
        HighGui.imshow("Krawędzie (15, 50)", edgesLowThreshold);
        HighGui.imshow("Krawędzie (40, 80)", edgesBalanced);
        HighGui.imshow("Krawędzie (70, 110)", edgesHighThreshold);

        // Poczekaj na klawisz i zamknij program
        HighGui.waitKey();
        System.exit(0);
    }

    // *** ZADANIE 2 ***
    // Funkcja do wykrywania krawędzi metodą Laplacian
    private void edgesLaplican(Mat image) {
        // Konwersja na skalę szarości
        Mat grayImage = new Mat();
        Imgproc.cvtColor(image, grayImage, Imgproc.COLOR_BGR2GRAY);
        // Wedle dokumentacji zalecane jest jeszcze użyć filtru GAUSSA
        Imgproc.GaussianBlur(grayImage, grayImage, new Size(3, 3), 0, 0, Core.BORDER_DEFAULT);
        // BORDER_DEFAULT = BORDER_REFLECT101 (int 4)

        // Wykrywanie krawędzi z różnymi parametrami
        Mat laplSmall = new Mat();
        Mat laplMedi = new Mat();
        Mat laplLarg = new Mat();

        /* Metoda Laplacian przyjmuje parametry:
        grayImage - bazową macierz z obrazem
        laplSmall - Obraz na wyjściu
        CvType.CV_.. - Głębokość obrazu docelowego
        ksize: Rozmiar kernela (jądra) czyli macierzy naszego filtra
        scale: prawdopodobnie skala obrazu wynikowego ale nie jestem pewien bo nie ma o tym
        informacj w dokumentacji, oprócz tego żeby ustawić ją na 1
        delta: tak samo z deltą brak informacji w dokumentacji, wartość domyślna to 0
        BORDER - Rodzaj wykonywanej ekstrapolacji w macierzys
         */
        Imgproc.Laplacian(grayImage, laplSmall, CvType.CV_16S,3, 1,0, Core.BORDER_DEFAULT );
        Imgproc.Laplacian(grayImage, laplMedi, CvType.CV_16U,5, 1,0, Core.BORDER_REFLECT );
        Imgproc.Laplacian(grayImage, laplLarg, CvType.CV_16S,7, 1,0, Core.BORDER_CONSTANT );

        // Konwersja do formatu CV_8U dla lepszej wizualizacji
        Mat absSmall = new Mat();
        Mat absMedium = new Mat();
        Mat absLarge = new Mat();
        Core.convertScaleAbs(laplSmall, absSmall);
        Core.convertScaleAbs(laplMedi, absMedium);
        Core.convertScaleAbs(laplLarg, absLarge);

        HighGui.imshow("Oryginalny obraz", image);
        HighGui.imshow("Laplacian (Kernel 3x3)", absSmall);
        HighGui.imshow("Laplacian (Kernel 5x5)", absMedium);
        HighGui.imshow("Laplacian (Kernel 7x7)", absLarge);

        HighGui.waitKey();
        System.exit(0);
    }
    // ***ZADANIE 3 ***
    private void edgesSobel(Mat image) {
        Mat grayImage = new Mat();
        Imgproc.cvtColor(image, grayImage, Imgproc.COLOR_BGR2GRAY);

        Mat gradX = new Mat();
        Mat gradY = new Mat();
        Mat sobelCombined = new Mat();

        // Parametry: Sobel (grayImage, output, ddepth, dx, dy, ksize, scale, delta, borderType)
        // Gradient w kierunku x
        Imgproc.Sobel(grayImage, gradX, CvType.CV_16S, 1, 0, 5, 1, 0, Core.BORDER_DEFAULT);
        // Gradient w kierunku y
        Imgproc.Sobel(grayImage, gradY, CvType.CV_16S, 0, 1, 5, 1, 0, Core.BORDER_DEFAULT);
        // Łączenie gradientów (dodawanie wartości bezwzględnych)
        Mat absGradX = new Mat();
        Mat absGradY = new Mat();
        Core.convertScaleAbs(gradX, absGradX);
        Core.convertScaleAbs(gradY, absGradY);
        // Dodawanie Macierzy X i macierzy Y
        Core.addWeighted(absGradX, 0.5, absGradY, 0.5, 0, sobelCombined);

        // Wyświetlenie wyników
        HighGui.imshow("Oryginalny obraz", image);
        HighGui.imshow("Gradient Sobel w kierunku X", absGradX);
        HighGui.imshow("Gradient Sobel w kierunku Y", absGradY);
        HighGui.imshow("Gradient Sobel połączony", sobelCombined);

        HighGui.waitKey();
        System.exit(0);
    }

    // *** ZADANIE 4***
    private void reduceEdges(Mat image) {
        Mat grayImage = new Mat();
        Imgproc.cvtColor(image, grayImage, Imgproc.COLOR_BGR2GRAY);

        // Najpierw Canny
        Mat edges = new Mat();
        Imgproc.Canny(grayImage, edges, 40, 70);

        // Użycie erozji
        Mat gausessEdges = new Mat();
        Imgproc.GaussianBlur(edges, gausessEdges, new Size(7, 7), 0, 0, Core.BORDER_DEFAULT);

        HighGui.imshow("Oryginalny obraz", image);
        HighGui.imshow("Krawędzie (Canny)", edges);
        HighGui.imshow("Krawędzie po rozmyciu Gaussa", gausessEdges);

        HighGui.waitKey();
        System.exit(0);
    }

    // ***ZADANIE 5***
    private void drawCounturs(Mat image) {
        // Skala szarości (jest bardzo popularna z tego co zauważyłem w tych operacjach)
        Mat grayImage = new Mat();
        Imgproc.cvtColor(image, grayImage, Imgproc.COLOR_BGR2GRAY);

        // Binaryzacja obrazu (progowanie)
        Mat binaryImage = new Mat();
        Imgproc.threshold(grayImage, binaryImage, 127, 255, Imgproc.THRESH_BINARY);

        // Zrobienie krawędzi za pomocą Canny
        Mat edges = new Mat();
        Imgproc.Canny(binaryImage, edges, 50, 150);

        // Lista wykrytych konturów
        List<MatOfPoint> contours = new ArrayList<>();
        Mat hierarchy = new Mat();
        Imgproc.findContours(edges, contours, hierarchy, Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_SIMPLE);

        // Rysowanie konturów na nowym obrazie
        Mat contourImage = Mat.zeros(image.size(), CvType.CV_8UC3); // Obraz na którym rysujemy kontury
        Imgproc.drawContours(contourImage, contours, -1, new Scalar(0, 255, 0), 2);

        // Wyświetlenie wyników
        HighGui.imshow("Oryginalny obraz", image);
        HighGui.imshow("Obraz po binaryzacji", binaryImage);
        HighGui.imshow("Krawędzie (Canny)", edges);
        HighGui.imshow("Kontury", contourImage);

        HighGui.waitKey();
        System.exit(0);
    }

    public Lab06(){
        String graphic = "zapisane.jpg";
        String figures = "figury.jpg";
        Mat image = loadImage(graphic);
        Mat fimage = loadImage(figures);
        // CannyEdges(image); // ZADANIE 1
        //edgesLaplican(image); //ZADANIE 2
        //edgesSobel(image); //ZADANIE 3
        //reduceEdges(image); //ZADANIE 4
        drawCounturs(fimage);
    }

}
