import org.opencv.core.*;
import org.opencv.highgui.HighGui;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import java.util.ArrayList;
import java.util.List;

public class Lab07 {
        final static int GAUSSIAN = 1;
        final static int MEDIAN = 2;
        final static int MORPH_OPEN = 3;
        final static int MORPH_CLOSE = 4;
        static{
            System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        }
        private Mat loadImage(String imagePath) {
            // Dla wczytywania obrazów z kanałem alfa musiałem zmodyfikować
            // Metodę wczytującą obrazy dodając parametr IMREAD_UNCHANGED
            Mat image = Imgcodecs.imread(imagePath, Imgcodecs.IMREAD_UNCHANGED);
            if (image.empty()) {
                System.out.println("Nie udało się wczytać obrazu: " + imagePath);
                System.exit(1);
            }
            return image;
        }

    // Funkcja do podmiany tła ZADANIE 1
    // Musiała zostać poprawiona bo powodowała błędy
    private Mat replaceBackground(String backgroundPath, String selfiePath) {
        Mat background = loadImage(backgroundPath);
        Mat selfie = loadImage(selfiePath);

        // Usuń kanał alfa, jeśli istnieje
        if (background.channels() == 4) {
            Imgproc.cvtColor(background, background, Imgproc.COLOR_BGRA2BGR);
        }
        if (selfie.channels() == 4) {
            Imgproc.cvtColor(selfie, selfie, Imgproc.COLOR_BGRA2BGR);
        }

        // Dopasowanie rozmiaru obrazów
        Imgproc.resize(background, background, selfie.size());

        // Konwersja selfie do HSV
        Mat hsvSelfie = new Mat();
        Imgproc.cvtColor(selfie, hsvSelfie, Imgproc.COLOR_BGR2HSV);

        // Zakres koloru zielonego
        Scalar lowerGreen = new Scalar(35, 55, 55);  // Dolny próg
        Scalar upperGreen = new Scalar(85, 255, 255); // Górny próg

        // Utworzenie maski dla zielonego ekranu
        Mat mask = new Mat();
        Core.inRange(hsvSelfie, lowerGreen, upperGreen, mask);

        // Pokasowałem resztę masek z różnymi filtrami
        Mat morphCloseMask = smoothEdges(mask, MORPH_CLOSE);

        // Odwrócenie maski
        Mat invertedMask = new Mat();
        Core.bitwise_not(morphCloseMask, invertedMask);

        // Wyciągnięcie tła i selfie
        Mat backgroundPart = new Mat();
        Mat selfiePart = new Mat();
        Core.bitwise_and(background, background, backgroundPart, morphCloseMask);
        Core.bitwise_and(selfie, selfie, selfiePart, invertedMask);

        // Scalanie tła i selfie
        Mat result = new Mat();
        Core.add(backgroundPart, selfiePart, result);

        // Wyświetlenie wyników
        //HighGui.imshow("Oryginalne tło", background);
        //HighGui.imshow("Selfie przed zielonym ekranem", selfie);
        //HighGui.imshow("Maska (oryginalna)", mask);
        //HighGui.imshow("Maska (Gaussian)", gaussianMask);
        // HighGui.imshow("Maska (Median)", medianMask);
        //HighGui.imshow("Maska (Otwarcie)", morphOpenMask);
        //HighGui.imshow("Maska (Domknięcie)", morphCloseMask);
        //HighGui.imshow("Obraz z podmienionym tłem (MEDIANA))", result);

        //HighGui.waitKey();
        //System.exit(0);

        return result;
    }


    private Mat smoothEdges(Mat mask, int method) {
        Mat smoothedMask = new Mat();

        switch (method) {
            case GAUSSIAN:
                // Filtr Gaussa
                Imgproc.GaussianBlur(mask, smoothedMask, new Size(5, 5), 0);
                break;

            case MEDIAN:
                // Filtr medianowy
                Imgproc.medianBlur(mask, smoothedMask, 5);
                break;

            case MORPH_OPEN:
                // Otwarcie morfologiczne
                Mat kernelOpen = Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(5, 5));
                Imgproc.morphologyEx(mask, smoothedMask, Imgproc.MORPH_OPEN, kernelOpen);
                break;

            case MORPH_CLOSE:
                // Domknięcie morfologiczne
                Mat kernelClose = Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(5, 5));
                Imgproc.morphologyEx(mask, smoothedMask, Imgproc.MORPH_CLOSE, kernelClose);
                break;

            default:
                System.out.println("Nieznana metoda wygładzania: " + method);
                return mask; // Jeśli metoda nieznana, zwracamy oryginalną maskę
        }

        return smoothedMask;
    }

    private void applyFrame(Mat image, String framePath) {
        Mat frame = loadImage(framePath); // Wczytaj ramkę z kanałem alfa

        // Sprawdź, czy ramka zawiera kanał alfa
        if (frame.channels() != 4) {
            System.out.println("Ramka nie zawiera kanału alfa. Przerywam działanie.");
            return;
        }

        // Dopasowanie rozmiaru ramki do obrazu
        Imgproc.resize(frame, frame, image.size());

        // Rozdzielenie ramki na kanały: B, G, R, Alpha
        List<Mat> frameChannels = new ArrayList<>();
        Core.split(frame, frameChannels);
        Mat alphaChannel = frameChannels.get(3); // Kanał alfa

        // Utworzenie maski i odwróconej maski z kanału alfa
        Mat mask = new Mat();
        Mat invertedMask = new Mat();
        Imgproc.threshold(alphaChannel, mask, 0, 255, Imgproc.THRESH_BINARY);
        Core.bitwise_not(mask, invertedMask);

        // Przygotowanie części ramki (RGB bez kanału alfa)
        Mat frameRGB = new Mat();
        Core.merge(frameChannels.subList(0, 3), frameRGB);

        // Wyodrębnienie tła z obrazu oraz ramki
        Mat imageBackground = new Mat();
        Mat frameForeground = new Mat();
        Core.bitwise_and(image, image, imageBackground, invertedMask);
        Core.bitwise_and(frameRGB, frameRGB, frameForeground, mask);

        // Połączenie obrazu z ramką
        Mat combined = new Mat();
        Core.add(imageBackground, frameForeground, combined);

        // Wyświetlenie wyniku
        HighGui.imshow("Obraz z nałożoną ramką", combined);
        HighGui.waitKey();
    }


    // Konstruktor
    public Lab07() {
        String backgroundPath = "tlo.jpg"; // Ścieżka do tła
        String selfiePath = "mieciu.png";         // Ścieżka do selfie
        String brightSelfie = "brigthermeciu.png";
        String framePath = "ramka.png";
        // ZADANIE 1
        Mat new_image = replaceBackground(backgroundPath, selfiePath);
        // Zadanie 3 replaceBackground(backgroundPath, brightSelfie);
        // Zadanie 4
        applyFrame(new_image, framePath);
    }
}



