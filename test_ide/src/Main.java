import org.opencv.core.Core;
import org.opencv.core.CvException;

public class Main {
    public static void main(String[] args) {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        //new Lab01();
        //new Lab02();
        //new Lab03();
        //new Lab03Zad3();
        //new Lab04();
        //new Lab05();
        //new Lab06();
        new Lab07();
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
