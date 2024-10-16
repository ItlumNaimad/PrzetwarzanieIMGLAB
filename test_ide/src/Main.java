import org.opencv.core.*;
import org.opencv.highgui.HighGui;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import static org.opencv.imgproc.Imgproc.putText;

public class Main {
    public static void main(String[] args) {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        /* ZAD1
        Mat img = new Mat(3, 3, CvType.CV_8U);
        int row = 0, col = 0;
        img.put(row, col, 1, 2, 3, 3, 5, 6, 6, 7, 8);
        System.out.println(img.dump());
        */
        /*ZAD2
        // Wczytujemy obraz z pliku do któego podana jest ścieżka jako parametr
        // funkcji Imgcodecs z metodą .imread()
        Mat img = Imgcodecs.imread("C:\\Users\\naima\\Desktop\\cosie\\mietczynski.jpg");
        Imgcodecs.imwrite("zapisane.jpg", img);
        // z tego co wyczytałem to jeszcze
        //do wczytywania do pamięci używa się cv::imdecode i cv::imencode
         */
        /*ZAD4
        okienko("zapisane.jpg", "obraz", true);
        //ZAD5
        okienko("obrazbmp.bmp","tobmp", false);
        okienko("obrazgif.gif","togif", false);
        okienko("zapispng.png","topng", false); */
        Mat img = Imgcodecs.imread("zapisane.jpg");
        Point org = new Point(0, 360);
        int fontType = Imgproc.FONT_HERSHEY_TRIPLEX;
        int fontSize = 2;
        int thickness = 2;
        Scalar color = new Scalar(180, 100, 155);
        Imgproc.putText(img, "NAPRAWDE", org, fontType, fontSize, color, thickness);
        HighGui.namedWindow("zad6", HighGui.WINDOW_NORMAL);
        HighGui.imshow("zad6", img);
        HighGui.waitKey(0);
    }
    /*
    Funkcja okienko przyjmuje parametry
    filename - nazwa pliku
    nameWin - nazwa okienka
    Najpierw tworzy macierz z wczytanego obrazka następnie
    z użyciem funkcji i metod od HighGui tworzy okienko
    umieszcza w nim obraz i czeka z okienkiem dopóki go nie wyłączymy
     */
    public static void okienko(String pathFile, String nameWin, boolean isGray) {
        Mat img = Imgcodecs.imread(pathFile);
        HighGui.namedWindow(nameWin, HighGui.WINDOW_AUTOSIZE);
        if (isGray) {
            //ZAD4
            // nowa zmienna gray macierz z przekształceniem skali szarości
            Mat gray = new Mat();
            // Konwertujemy kolorowy obraz do skali szarości
            // parametr img wykorzystany z wcześniej wczytanej
            // macierzy na których były przeprowadzane operacje
            Imgproc.cvtColor(img, gray, Imgproc.COLOR_BGR2GRAY);
            HighGui.imshow(nameWin, gray);
        }
        else
            HighGui.imshow(nameWin, img);
        HighGui.waitKey();
    }
}

/* JAKIŚ EXAMPLE

// Importing all OpenCV files
import org.opencv.*;
import org.opencv.imgproc.Imgproc;

public class GFG {

    // Main driver code
    public static void main(String args[]) throws Exception
    {

        // Loading the OpenCV core library
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

        // Reading the contents of the image
        // from local computer directory
        String src = "D:\\InputImage.jpg";

        // Creating a Mat object
        Mat image = Imgcodecs.imread(src);

        // Text to be added
        String text = "GFG IS COOL";

        // Points from where text should be added
        Point org = new Point(170, 280);

        // Color of the text
        Scalar color = new Scalar(0, 0, 255);

        // Fonttype of the text to be added
        int fontType = Imgproc.FONT_HERSHEY_PLAIN;

        // Fontsize of the text to be added
        int fontSze = 1;

        // Thickness of the lines in px
        int thickness = 3;

        // Adding text to the image using putText method
        Imgproc.putText(image, text, org, fontType,
                        fontSize, color, thickness);

        // Displaying the Image after adding the Text
        HighGui.imshow("", image);

        // Waiting for a key event to delay
        HighGui.waitKey();
    }
}


KOLEJNE NOTATKI
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Point;
import org.opencv.core.RotatedRect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.highgui.HighGui;
public class DrawingGeometricalShapes {
   public static void main(String args[]) {
      System.loadLibrary( Core.NATIVE_LIBRARY_NAME );
      Mat src = Imgcodecs.imread("D:\blank.jpg");
      Scalar color = new Scalar(0, 0, 120);
      //Drawing a Circle
      Imgproc.circle(src, new Point(75, 65), 40, color, Imgproc.FILLED);
      // Drawing an Ellipse
      Imgproc.ellipse(src, new RotatedRect(new Point(330, 60), new Size(100, 65), 180), color, Imgproc.FILLED);
      //Drawing a line
      Imgproc.line(src, new Point(540,30), new Point(540, 90), color, 5);
      //Drawing filled polygon
      List<MatOfPoint> list = new ArrayList();
      list.add(new MatOfPoint (
         new Point(410, 60), new Point(430, 30),
         new Point(470, 30), new Point(490, 60),
         new Point(470, 100), new Point(430, 100))
      );
      Imgproc.fillPoly (src, list, color, 8);
      //Drawing a Rectangle
      Imgproc.rectangle(src, new Point(150, 30), new Point(250, 95),color, Imgproc.FILLED);
      HighGui.imshow("Geometrical shapes", src);
      HighGui.waitKey();
   }
}
*/
