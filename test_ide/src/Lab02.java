import org.opencv.core.*;
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
        /* ZAD2
        // Macierze odbić obrazu
        Mat pionowo = new Mat();
        Mat poziomo = new Mat();
        Mat pionpoziom = new Mat();

        // Odbicie obrazu w pionie
        Core.flip(img, pionowo, 0); // flipCode = 0 (odbicie w pionie)

        // Odbicie obrazu w poziomie
        Core.flip(img, poziomo, 1); // flipCode = 1 (odbicie w poziomie)

        // Odbicie obrazu w pionie i poziomie
        Core.flip(img, pionpoziom, -1); // flipCode = 1 (odbicie w poziomie)
         */
        // ZAD3
        //**TUTAJ NALEŻY PODAĆ ŻADANY KĄT ORBOTU**
        /*
        double kat_obrotu = 45;
        // wyżej mamy kąt orbotu, a niżej mamy punkt centralny obrotu
        Point center = new Point(img.width() / 2, img.height() / 2);
        // Oblicz macierz rotacji (obrót o kat_obrotu stopni)
        //To chyba jedna z dłużyszch metod
        Mat rotationMatrix = Imgproc.getRotationMatrix2D(center, kat_obrotu, 1.0);
        // Stwórz macierz do przechowywania obróconego obrazu
        Mat obrocony = new Mat();
        // Zastosuj rotację
        Imgproc.warpAffine(img, obrocony, rotationMatrix, new Size(img.width(), img.height()));
         */
        /* ** ZADANIE 4 **
        // Definicja prostokątu do wycięcia
        Rect prostokat = new Rect(50, 50, 200, 150);
        // Utwórz nową macierz o takich samych rozmiarach jak ROI
        Mat wyciecie = new Mat(prostokat.height, prostokat.width, img.type(), new Scalar(0));
        // Kopia fragmentut obrazu do nowej macierzy
        // funkcja submat
        img.submat(prostokat).copyTo(wyciecie);
        HighGui.namedWindow("obraz1", HighGui.WINDOW_NORMAL);
        HighGui.imshow("obraz1", wyciecie);
        // ** DRUGI SPOSÓB**
        // Definicja prostokątu  do wycięcia
        // Wartości: x, y, szerokość, wysokość
        Rect prostokat2 = new Rect(50, 50, 200, 150);

        // Funkcja submat() wycina
        Mat wyciecie2 = img.submat(prostokat2);
        */
        //**ZADANIE 5**
        // METODA RESIZE()
        Mat resizedImg2x = new Mat();
        // Metoda resize() wymaga przemnożenia przez żądane wielkości
        Imgproc.resize(img, resizedImg2x, new Size(img.width() * 0.5, img.height() * 0.5));
        // Nie wpisywałem nowego kodu tylk zedytowałem liczby użyte do obliczeń
        // W metodzie resize użyłem wartości ułamkowych by pomniejszyć obraz
        // Powiększa 4-krotnie za pomocą resize
        Mat resizedImg4x = new Mat();
        Imgproc.resize(img, resizedImg4x, new Size(img.width() * 0.25, img.height() * 0.25));

        // Zapis obrazków na dysku
        Imgcodecs.imwrite("zapisane_05x.jpg", resizedImg2x);
        Imgcodecs.imwrite("zapisane_025x.jpg", resizedImg4x);

        System.out.println("Pomniejszone obrazy zostały zapisane.");

        // ** METODA pyrUP() **
        // Powiększenie metodoą pyrUp
        // Metoda pyrUp() powiększa dwurkotnie obraz
        // Metoda pyrDown() odwrotnie
        Mat pyrDownImg2x = new Mat();
        Imgproc.pyrDown(img, pyrDownImg2x);

        // Powiększenie 4 krotne wymaga podwójnego użycia pyrUpu()
        // Natomiast pomniejszenie wymaga pyrDown()
        Mat pyrDownImg4x = new Mat();
        Imgproc.pyrDown(pyrDownImg2x, pyrDownImg4x);

        // Zapisz powiększone obrazy na dysku
        Imgcodecs.imwrite("pyrDown_2x.jpg", pyrDownImg2x);
        Imgcodecs.imwrite("pyrDown_4x.jpg", pyrDownImg4x);

        System.out.println("Pomniejszone obrazy zostały zapisane.");

        //HighGui.namedWindow("obraz2", HighGui.WINDOW_NORMAL);
        //HighGui.imshow("obraz2", wyciecie2);
        //HighGui.waitKey();


    }

}
