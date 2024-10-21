import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.highgui.HighGui;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

public class Lab02 {
    public Lab02(){
        System.loadLibrary(org.opencv.core.Core.NATIVE_LIBRARY_NAME);
        Mat img = Imgcodecs.imread("zapisane.jpg");

        // Check if image is loaded fine
        if( img.empty() ) {
            System.out.println("Error opening image!");
            System.out.println("Program Arguments: [image_name -- default ../data/lena.jpg] \n");
            System.exit(-1);
        }

        /* ZAD1
        // Wartości przesunięcia w pikselach
        int Tx = 50; // Przesunięcie w osi X (prawo)
        int Ty = 30; // Przesunięcie w osi Y (dół)
        // Macierz transformacji (translacji)
        Mat transMat = Mat.zeros(2, 3, CvType.CV_32F);
        transMat.put(0, 0, 1); // Skalowanie w osi X
        transMat.put(1, 1, 1); // Skalowanie w osi Y
        transMat.put(0, 2, Tx); // Przesunięcie w osi X
        transMat.put(1, 2, Ty); // Przesunięcie w osi Y
        //Macierz [(1,0,Tx),(0,1,Ty)]
        //Macierz translacji a potem zastosowanie transformacji
        Mat shiftedImg = new Mat();
        Imgproc.warpAffine(img, shiftedImg, transMat, new Size(img.cols(), img.rows()));
        */

        // Macierze odbić obrazu
        Mat pionowo = new Mat();
        Mat poziomo = new Mat();
        Mat pionpoziom = new Mat();

        // Odbicie obrazu w pionie
        //Core.flip(img, pionowo, 0); // flipCode = 0 (odbicie w pionie)

        // Odbicie obrazu w poziomie
        //Core.flip(img, poziomo, 1); // flipCode = 1 (odbicie w poziomie)

        // Odbicie obrazu w pionie i poziomie
        Core.flip(img, pionpoziom, -1); // flipCode = 1 (odbicie w poziomie)
        HighGui.namedWindow("obraz", HighGui.WINDOW_AUTOSIZE);
        HighGui.imshow("obraz", pionpoziom);
        HighGui.waitKey();

    }
    public static void okienko(String pathFile, String nameWin) {
        Mat img = Imgcodecs.imread(pathFile);
        HighGui.namedWindow(nameWin, HighGui.WINDOW_AUTOSIZE);
        HighGui.imshow(nameWin, img);
        HighGui.waitKey();
    }
}
