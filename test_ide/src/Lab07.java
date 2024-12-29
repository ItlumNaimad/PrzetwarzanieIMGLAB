import org.opencv.core.*;
import org.opencv.highgui.HighGui;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;
import org.opencv.videoio.VideoCapture;
import org.opencv.videoio.VideoWriter;
import org.opencv.videoio.Videoio;

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
    // Do zadania 5 użyłem przeciążenia funkcji replace Background
    private Mat replaceBackground(String backgroundPath, String framePath) {
        // Wczytanie obrazów z podanych ścieżek
        Mat background = loadImage(backgroundPath);
        Mat frame = loadImage(framePath);

        // Wywołanie przeciążonej wersji funkcji
        return replaceBackground(background, frame);
    }

    private Mat replaceBackground(Mat background, Mat frame) {
        // Usuń kanał alfa, jeśli istnieje
        if (background.channels() == 4) {
            Imgproc.cvtColor(background, background, Imgproc.COLOR_BGRA2BGR);
        }
        if (frame.channels() == 4) {
            Imgproc.cvtColor(frame, frame, Imgproc.COLOR_BGRA2BGR);
        }

        // Dopasowanie rozmiaru tła do klatki wideo
        Imgproc.resize(background, background, frame.size());

        // Konwersja klatki do HSV
        Mat hsvFrame = new Mat();
        Imgproc.cvtColor(frame, hsvFrame, Imgproc.COLOR_BGR2HSV);

        // Zakres koloru zielonego
        Scalar lowerGreen = new Scalar(125, 200, 200);  // Dolny próg
        Scalar upperGreen = new Scalar(85, 255, 255); // Górny próg

        // Utworzenie maski dla zielonego ekranu
        Mat mask = new Mat();
        Core.inRange(hsvFrame, lowerGreen, upperGreen, mask);

        // Sprawdzenie, czy maska zawiera jakikolwiek zielony piksel
        if (Core.countNonZero(mask) == 0) {
            // Jeśli brak zielonego, dodaj tekst „No greenscreen” na klatce
            Imgproc.putText(
                    frame,                            // Obraz, na którym dodajemy tekst
                    "No greenscreen",                 // Tekst do wyświetlenia
                    new Point(250, 450),                // Pozycja tekstu
                    Imgproc.FONT_HERSHEY_SIMPLEX,     // Czcionka
                    5.0,                              // Rozmiar czcionki
                    new Scalar(0, 0, 255),            // Kolor tekstu (czerwony)
                    5                                 // Grubość tekstu
            );
            return frame; // Zwracamy oryginalną klatkę z tekstem
        }

        // Wygładzenie krawędzi maski
        Mat smoothedMask = new Mat();
        Mat kernelClose = Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(5, 5));
        Imgproc.morphologyEx(mask, smoothedMask, Imgproc.MORPH_CLOSE, kernelClose);

        // Odwrócenie maski
        Mat invertedMask = new Mat();
        Core.bitwise_not(smoothedMask, invertedMask);

        // Wyciągnięcie tła i klatki
        Mat backgroundPart = new Mat();
        Mat framePart = new Mat();
        Core.bitwise_and(background, background, backgroundPart, smoothedMask);
        Core.bitwise_and(frame, frame, framePart, invertedMask);

        // Scalanie tła i klatki
        Mat result = new Mat();
        Core.add(backgroundPart, framePart, result);

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
        // Scalanie tła i klatki

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
    // ***ZADANIE 5***
    private void processVideo(String videoPath, String backgroundPath, String outputPath) {
        VideoCapture capture = new VideoCapture(videoPath);
        if (!capture.isOpened()) {
            System.out.println("Nie udało się otworzyć pliku wideo: " + videoPath);
            return;
        }

        // Pobranie rozmiaru i liczby klatek na sekundę wideo
        int frameWidth = (int) capture.get(Videoio.CAP_PROP_FRAME_WIDTH);
        int frameHeight = (int) capture.get(Videoio.CAP_PROP_FRAME_HEIGHT);
        int fps = (int) capture.get(Videoio.CAP_PROP_FPS);

        VideoWriter writer = new VideoWriter(outputPath, VideoWriter.fourcc('M', 'J', 'P', 'G'), fps, new Size(frameWidth, frameHeight));

        Mat background = loadImage(backgroundPath);
        Mat frame = new Mat();

        while (capture.read(frame)) {
            Mat processedFrame = replaceBackground(background, frame);
            writer.write(processedFrame);

            // Wyświetlanie przetwarzanej klatki (opcjonalnie)
            HighGui.imshow("Przetwarzanie wideo", processedFrame);
            if (HighGui.waitKey(1) == 27) { // Przerwanie po wciśnięciu ESC
                break;
            }
        }
    }
    // ***ZADANIE 7 ***
    private void faceDetect(Mat image, String cascadePath)
    {
        // Konwersja obrazu do skali szarości
        Mat grayImage = new Mat();
        Imgproc.cvtColor(image, grayImage, Imgproc.COLOR_BGR2GRAY);

        // Wyrównanie histogramu
        Imgproc.equalizeHist(grayImage, grayImage);

        CascadeClassifier faceCascade = new CascadeClassifier(cascadePath);

        if (faceCascade.empty()) {
            System.out.println("Nie udało się załadować klasyfikatora Haar Cascade.");
            return;
        }

        // Wykrywanie twarzy
        MatOfRect faces = new MatOfRect();
        faceCascade.detectMultiScale(grayImage, faces);

        // Zaznaczanie wykrytych twarzy na obrazie
        for (Rect face : faces.toArray()) {
            Imgproc.rectangle(image,
                    new Point(face.x, face.y),
                    new Point(face.x + face.width, face.y + face.height),
                    new Scalar(0, 255, 0), 2); // Zielony prostokąt o grubości 2
        }

        // Zapis obrazu z zaznaczoną twarzą
        String outputPath = "detected1.jpg"; // Zamień na ścieżkę do pliku wyjściowego
        Imgcodecs.imwrite(outputPath, image);

        System.out.println("Zapisano obraz z wykrytą twarzą: " + outputPath);
        HighGui.imshow("FaceDetect", image);
        HighGui.waitKey();
        System.exit(0);
    }
    // ***ZADANIE 8 ***
    public void trackFaceInVideo(String videoPath, String cascadePath, String outputPath) {
        // Załaduj klasyfikator Haar Cascade
        CascadeClassifier faceCascade = new CascadeClassifier(cascadePath);

        if (faceCascade.empty()) {
            System.out.println("Nie udało się załadować klasyfikatora Haar Cascade.");
            return;
        }

        // Wczytaj wideo
        VideoCapture videoCapture = new VideoCapture(videoPath);
        if (!videoCapture.isOpened()) {
            System.out.println("Nie udało się otworzyć wideo.");
            return;
        }

        // Przygotowanie zapisu wideo
        Size frameSize = new Size((int) videoCapture.get(Videoio.CAP_PROP_FRAME_WIDTH),
                (int) videoCapture.get(Videoio.CAP_PROP_FRAME_HEIGHT));
        int fourcc = VideoWriter.fourcc('M', 'J', 'P', 'G'); // Format zapisu wideo
        double fps = videoCapture.get(Videoio.CAP_PROP_FPS);
        VideoWriter videoWriter = new VideoWriter(outputPath, fourcc, fps, frameSize);

        if (!videoWriter.isOpened()) {
            System.out.println("Nie udało się utworzyć pliku wyjściowego.");
            return;
        }

        Mat frame = new Mat();
        Mat grayFrame = new Mat();

        while (videoCapture.read(frame)) {
            // Konwersja klatki do skali szarości
            Imgproc.cvtColor(frame, grayFrame, Imgproc.COLOR_BGR2GRAY);

            // Wyrównanie histogramu
            Imgproc.equalizeHist(grayFrame, grayFrame);

            // Wykrywanie twarzy
            MatOfRect faces = new MatOfRect();
            faceCascade.detectMultiScale(grayFrame, faces);

            // Zaznaczenie wykrytych twarzy na klatce
            for (Rect face : faces.toArray()) {
                Imgproc.rectangle(frame,
                        new Point(face.x, face.y),
                        new Point(face.x + face.width, face.y + face.height),
                        new Scalar(0, 255, 0), // Zielony prostokąt
                        2); // Grubość linii
            }

            // Zapis przetworzonej klatki do pliku wideo
            videoWriter.write(frame);

            // Opcjonalne wyświetlenie przetwarzanej klatki
            HighGui.imshow("Śledzenie twarzy", frame);
            if (HighGui.waitKey(1) == 27) { // Przerwij po wciśnięciu ESC
                break;
            }
        }

        // Zwolnienie zasobów
        videoCapture.release();
        videoWriter.release();
        HighGui.destroyAllWindows();
        System.out.println("Zapisano przetworzone wideo: " + outputPath);
    }

    // Konstruktor
    public Lab07() {
        String backgroundPath = "tlo.jpg"; // Ścieżka do tła
        String selfiePath = "mieciu.png";         // Ścieżka do selfie
        String brightSelfie = "brigthermeciu.png";
        String framePath = "ramka.png";
        // Do zadania 5 i 8
        String videoPath = "mietczynskivid.mkv"; // Ścieżka do wideo
        String outputPath = "output2.avi"; // Ścieżka do zapisanego wideo
        String cascadePath = "haarcascade_frontalface_default.xml"; // Ścieżka do pliku XML
        String outputPath2 = "output2.avi"; // Ścieżka do zapisanego wideo
        // ZADANIE 1
        //Mat new_image = replaceBackground(backgroundPath, selfiePath);
        // Zadanie 3 replaceBackground(backgroundPath, brightSelfie);
        // Zadanie 4
        //applyFrame(new_image, framePath);
        // ZADANIE 5
        //processVideo(videoPath, backgroundPath, outputPath);
        //System.out.println("Przetwarzanie wideo zakończone. Plik zapisany jako: " + outputPath);
        // ZADANIE 7
        //faceDetect(loadImage(selfiePath), cascadePath);
        // ZADANIE 8
        //trackFaceInVideo(videoPath, cascadePath, outputPath2);
        // ZADANIE 9
        //faceDetect(loadImage("ponizsze.JPG"), cascadePath);
        faceDetect(loadImage("zapisane.jpg"), cascadePath);
    }
}



