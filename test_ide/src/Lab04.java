import org.opencv.core.*;
import org.opencv.highgui.HighGui;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import java.util.ArrayList;
import java.util.List;

import static org.opencv.imgproc.Imgproc.*;

public class Lab04 {
    Mat image;
    Mat gray_image = new Mat();
    /*
    Najpierw trzeba wykonać binaryzację. Tą inicjalizuje się funkcją
    Imgproc.threshold(). Następnie wybieramy typ operacji erozji
    MORPH_RECT, MORPH_CROSS lub MORPH_ELLIPSE, które wybieramy
    liczbami całkowitymi 0, 1 lub 2 w parametrze funkcji
    (czyli zmienna type_for_operation).
    */
    public Mat erosionAndBinary(Mat grayImage, int type_for_operation, int dilateOrErode) {
        Mat img2 = new Mat();
        threshold(grayImage, img2, 127, 255, THRESH_BINARY);
        int kernelSize = 3;
        // Wybór typu operacji
        int elementType = 0;
        if (type_for_operation == 0) elementType = MORPH_RECT;
        else if (type_for_operation == 1) {
            elementType = MORPH_CROSS;
        } else if (type_for_operation == 2) {
            elementType = MORPH_ELLIPSE;
        }
        // Pozyskanie zmiennej element potrzebnej do funkcji erode()
        Mat element = getStructuringElement(elementType, new Size(2 * kernelSize + 1, 2 * kernelSize + 1),
                new Point(kernelSize, kernelSize));
        // Instrukcja warunkowa wybory dylatacji lub erozji
        if(dilateOrErode == 0) {
            erode(grayImage, img2, element);
        }
        else if (dilateOrErode == 1){
            dilate(grayImage, img2, element);
            dilate(img2, img2, element);
            dilate(img2, img2, element);
            dilate(img2, img2, element);
            dilate(img2, img2, element);
        } else  {
            System.out.println("dilateOrErode: " + dilateOrErode + "is unknow command!");
        }

        // Zapis pliku do dysku
        Imgcodecs.imwrite("C:\\Users\\naima\\Java\\Przetwrz_laby\\PrzetwarzanieIMGLAB\\test_ide\\lab4\\zad1.jpg",img2);
        return img2;
    }

    public TwoValues openAndClose(Mat grayImage, int type_for_operation) {
        int elementType = 0;
        if (type_for_operation == 0) {
            elementType = MORPH_RECT;
        } else if (type_for_operation == 1) {
            elementType = MORPH_CROSS;
        } else if (type_for_operation == 2) {
            elementType = MORPH_ELLIPSE;
        }
        // Tworzenie jądra do operacji morfologicznych
        Mat kernel = getStructuringElement(elementType, new Size(5, 5));
        // Otwarcie (Erozja -> Dilatacja)
        Mat opening = new Mat();
        morphologyEx(grayImage, opening, MORPH_OPEN, kernel);
        // Domknięcie (Dilatacja -> Erozja)
        Mat closing = new Mat();
        morphologyEx(grayImage, closing, MORPH_CLOSE, kernel);
        return new TwoValues(opening, closing); 
    }
    public Mat findAndDrawContours() {
        Mat binaryImage = new Mat();
        Mat drawing = new Mat();
        threshold(gray_image, binaryImage, 127, 255, THRESH_BINARY);
        List<MatOfPoint> contours = new ArrayList<>();
        Mat hierarchy = new Mat();
        Imgproc.findContours(binaryImage, contours, hierarchy, Imgproc.RETR_LIST, Imgproc.CHAIN_APPROX_TC89_L1);
        drawing = Mat.zeros(image.size(), CvType.CV_8UC3);
        for (int i = 0; i < contours.size(); i++) {
            Imgproc.drawContours(drawing, contours, i, new Scalar(0, 255, 0),
                    2, Imgproc.LINE_8, hierarchy, 0, new Point());
        }
        return  drawing;
    }
    public Lab04(){
        image = Imgcodecs.imread("zapisane.jpg");
        // Zmiana obrazu z macierzy image na skalę szarości

        cvtColor(image, gray_image, COLOR_BGR2GRAY);
        if (image.empty()) {
            System.out.println("Empty image: " + "zapisane.jpg");
            System.exit(0);
        }
        /*
        //Zadania od 1 do 4 poprzez edycję parametrów
        gray_image = erosionAndBinary(gray_image, 2, 1);
        HighGui.imshow("New Image", gray_image);
        */
        /*
        // dla Zadania 5
        // Przykładowe użycie metody openAndClose
        TwoValues results = openAndClose(gray_image, 0);
        Mat opened = results.getMat1();
        Mat closed = results.getMat2();

        HighGui.imshow("Otwarcie", opened);
        HighGui.imshow("Zamknięcie", closed);
         */
        // Zadanie 6
        Mat counturs = findAndDrawContours();
        HighGui.imshow("Counturs", counturs);
        HighGui.waitKey();
        System.exit(0);
    }
}
class TwoValues {
    public Mat mat1;
    public Mat mat2;
    public TwoValues(Mat mat1, Mat mat2) {
        this.mat1 = mat1;
        this.mat2 = mat2;
    }
    public Mat getMat1() {
        return mat1;
    } public Mat getMat2() {
        return mat2;
    }
}