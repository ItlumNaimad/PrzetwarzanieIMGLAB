import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.highgui.HighGui;
import org.opencv.imgcodecs.Imgcodecs;
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
    public Lab03()
    {
        Mat image = Imgcodecs.imread("zapisane.jpg");
        if (image.empty()) {
            System.out.println("Empty image: " + "zapisane.jpg");
            System.exit(0);
        }
        // Wywołanie funkcji contrast która zwraca macierz ze zmianą kontrastu
        Mat newImage = contrast(image);
        HighGui.imshow("Original Image", image);
        HighGui.imshow("New Image", newImage);
        HighGui.waitKey();
        System.exit(0);
    }

}
