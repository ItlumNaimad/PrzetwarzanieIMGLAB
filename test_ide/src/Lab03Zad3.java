import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.highgui.HighGui;
import org.opencv.imgcodecs.Imgcodecs;
import java.util.Locale;
import java.util.Scanner;
public class Lab03Zad3 {
    public Lab03Zad3() {
        double alpha = 0.5;
        double beta;
        double input;
        Mat src1, src2, dst = new Mat();
        System.out.println(" Simple Linear Blender ");
        System.out.println("-----------------------");
        System.out.println("* Enter alpha [0.0-1.0]: ");
        Scanner scan = new Scanner(System.in).useLocale(Locale.US);
        input = scan.nextDouble();
        if (input >= 0.0 && input <= 1.0)
            alpha = input;
        src1 = Imgcodecs.imread("LinuxLogo.jpg");
        src2 = Imgcodecs.imread("WindowsLogo.jpg");
        if (src1.empty() == true) {
            System.out.println("Error loading src1");
            return;
        }
        if (src2.empty() == true) {
            System.out.println("Error loading src2");
            return;
        }
        beta = (1.0 - alpha);
        Core.addWeighted(src1, alpha, src2, beta, 0.0, dst);
        HighGui.imshow("Linear Blend", dst);
        HighGui.waitKey(0);
        System.exit(0);
    }
}