import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Size;
import org.opencv.highgui.HighGui;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import static org.opencv.imgproc.Imgproc.*;

public class Lab04 {
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
        if (type_for_operation == 0) {
            elementType = MORPH_RECT;
        } else if (type_for_operation == 1) {
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
    public Lab04(){
        Mat image = Imgcodecs.imread("zapisane.jpg");
        // Zmiana obrazu z macierzy image na skalę szarości
        Mat gray_image = new Mat();
        cvtColor(image, gray_image, COLOR_BGR2GRAY);
        if (image.empty()) {
            System.out.println("Empty image: " + "zapisane.jpg");
            System.exit(0);
        }
        gray_image = erosionAndBinary(gray_image, 2, 1);
        HighGui.imshow("Original Image", image);
        HighGui.imshow("New Image", gray_image);
        HighGui.waitKey();
        System.exit(0);
    }
}
