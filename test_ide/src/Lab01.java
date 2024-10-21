import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.highgui.HighGui;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

public class Lab01 {
    public Lab01(){
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
        /* ZAD6
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
        */

        //ZAD7
        geometrical();
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
    public static void geometrical() {
        System.loadLibrary( Core.NATIVE_LIBRARY_NAME );
        Mat grafika = Imgcodecs.imread("zapisane.jpg");
        Scalar color = new Scalar(40, 120, 40); //FORMAT KOLORU BGR
        Scalar color1 = new Scalar(0, 120, 140); //kolor dla linii prostej
        Scalar color2 = new Scalar(210, 170, 100); //kolor dla prostokątu
        //Rysowanie koła
        Imgproc.circle(grafika, new Point(495, 265), 55, color, Imgproc.FILLED);
        //Prosta
        Imgproc.line(grafika, new Point(130,20), new Point(540, 111), color1, 5);
        //Drawing prostokąt
        Imgproc.rectangle(grafika, new Point(150, 50), new Point(250, 150),color2, Imgproc.FILLED);
        HighGui.imshow("figury", grafika);
        HighGui.waitKey();
    }
}

