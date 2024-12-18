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
        private Mat loadImage(String filePath) {
            Mat image = Imgcodecs.imread(filePath);
            if (image.empty()) {
                System.out.println("Nie udało się wczytać obrazu: " + filePath);
                System.exit(0);
            }
            return image;
        }
    // Funkcja do podmiany tła ZADANIE 1
    private Mat replaceBackground(String backgroundPath, String selfiePath) {
        // Wczytanie obrazów
        Mat background = loadImage(backgroundPath);
        Mat selfie = loadImage(selfiePath);

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

        // Poprawa maski za pomocą różnych metod
        Mat gaussianMask = smoothEdges(mask, GAUSSIAN);
        Mat medianMask = smoothEdges(mask, MEDIAN);
        Mat morphOpenMask = smoothEdges(mask, MORPH_OPEN);
        Mat morphCloseMask = smoothEdges(mask, MORPH_CLOSE);

        // Odwrócenie maski dla każdej metody
        Mat invertedMask = new Mat();
        Core.bitwise_not(morphCloseMask, invertedMask); // Przykład użycia maski z domknięciem
        //Core.bitwise_not(morphOpenMask, invertedMask); // Przykład użycia maski z otwarciem
        //Core.bitwise_not(medianMask, invertedMask); // Przykład użycia maski miedianowej
        //Core.bitwise_not(gaussianMask, invertedMask); // Przykład użycia maski gaussa
        // Wyciągnięcie tła i selfie
        Mat backgroundPart = new Mat();
        Mat selfiePart = new Mat();
        Core.bitwise_and(background, background, backgroundPart, morphOpenMask);
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

    // ZADANIE 4
    private void applyFrame(Mat baseImage, String framePath) {
        // Wczytanie obrazu ramki
        Mat frame = loadImage(framePath);

        // Dopasowanie rozmiaru ramki do obrazu
        Imgproc.resize(frame, frame, baseImage.size());

        // Rozdzielenie kanałów ramki na BGR i Alfa
        List<Mat> frameChannels = new ArrayList<>();
        Core.split(frame, frameChannels);

        if (frameChannels.size() < 4) {
            System.out.println("Obraz ramki nie zawiera kanału alfa.");
            return;
        }

        Mat frameAlpha = frameChannels.get(3); // Kanał alfa
        Mat frameBGR = new Mat();
        Core.merge(frameChannels.subList(0, 3), frameBGR); // Kanały BGR

        // Normalizacja kanału alfa do zakresu 0–1
        Mat normalizedAlpha = new Mat();
        frameAlpha.convertTo(normalizedAlpha, CvType.CV_32F, 1.0 / 255.0);

        // Konwersja obrazów do 32-bitowego formatu
        Mat baseImageFloat = new Mat();
        Mat frameBGRFloat = new Mat();
        baseImage.convertTo(baseImageFloat, CvType.CV_32FC3);
        frameBGR.convertTo(frameBGRFloat, CvType.CV_32FC3);

        // Odwrócenie kanału alfa
        Mat invertedAlpha = new Mat();
        Core.subtract(Mat.ones(normalizedAlpha.size(), CvType.CV_32F), normalizedAlpha, invertedAlpha);

        // Nakładanie ramki na obraz
        Core.multiply(frameBGRFloat, normalizedAlpha, frameBGRFloat); // Ramka z przezroczystością
        Core.multiply(baseImageFloat, invertedAlpha, baseImageFloat); // Obraz bez ramki
        Mat blended = new Mat();
        Core.add(frameBGRFloat, baseImageFloat, blended); // Dodanie obrazu i ramki

        // Konwersja do 8-bitowego obrazu
        blended.convertTo(blended, CvType.CV_8UC3);

        // Wyświetlenie wyniku
        HighGui.imshow("Oryginalny obraz", baseImage);
        HighGui.imshow("Ramka", frame);
        HighGui.imshow("Obraz z ramką", blended);

        HighGui.waitKey();
        System.exit(0);
    }

    // Konstruktor
    public Lab07() {
        String backgroundPath = "tlo.jpg"; // Ścieżka do tła
        String selfiePath = "mieciu.png";         // Ścieżka do selfieh
        String brightSelfie = "brigthermeciu.png";
        String framePath = "ramka.png";
        // ZADANIE 1
        Mat new_image = replaceBackground(backgroundPath, selfiePath);
        // Zadanie 3 replaceBackground(backgroundPath, brightSelfie);
        // Zadanie 4
        applyFrame(new_image, framePath);
    }
}



