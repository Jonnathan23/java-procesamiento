import java.awt.image.BufferedImage;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;
import java.io.File;
import javax.imageio.ImageIO;

public class gaussianTwoThreads {
    public static void main(String[] args) {
        try {
            String pathImage = "C:/Users/Jonna/Desktop/ups/8ctavo ciclo/paralelo/Filtros_java/imgs/digital_art.jpg";

            // Cargar la imagen original
            BufferedImage image = ImageIO.read(new File(pathImage));

            // Parámetros del filtro
            int kernelSize = 51; // Tamaño del kernel
            double sigma = 15;   // Desviación estándar (controla la intensidad del desenfoque)

            // Generar el kernel gaussiano
            float[] matrix = createGaussianKernel(kernelSize, sigma);
            Kernel kernel = new Kernel(kernelSize, kernelSize, matrix);
            ConvolveOp op = new ConvolveOp(kernel, ConvolveOp.EDGE_NO_OP, null);

            // Dividir la imagen en 2 mitades horizontales
            int width = image.getWidth();
            int height = image.getHeight();
            int midHeight = height / 2;

            BufferedImage topHalf = image.getSubimage(0, 0, width, midHeight);
            BufferedImage bottomHalf = image.getSubimage(0, midHeight, width, height - midHeight);

            BufferedImage topResult = new BufferedImage(width, midHeight, image.getType());
            BufferedImage bottomResult = new BufferedImage(width, height - midHeight, image.getType());

            // Crear hilos para cada mitad
            Thread thread1 = new Thread(() -> {
                op.filter(topHalf, topResult);
            });

            Thread thread2 = new Thread(() -> {
                op.filter(bottomHalf, bottomResult);
            });

            //  Medir tiempo de ejecución total
            long startTime = System.nanoTime();

            thread1.start();
            thread2.start();

            // Esperar a que ambos hilos terminen
            thread1.join();
            thread2.join();

            long endTime = System.nanoTime();

            // Combinar los resultados
            BufferedImage finalImage = new BufferedImage(width, height, image.getType());
            finalImage.createGraphics().drawImage(topResult, 0, 0, null);
            finalImage.createGraphics().drawImage(bottomResult, 0, midHeight, null);

            // Guardar la imagen resultante
            File output = new File("C:/Users/Jonna/Desktop/ups/8ctavo ciclo/paralelo/Filtros_java/imgs/digital_art_blur_parallel_dos_hilos.jpg");
            ImageIO.write(finalImage, "jpg", output);

            // Mostrar resultados
            System.out.println("✅ Filtro gaussiano aplicado en paralelo con 2 hilos.");
            System.out.println("Imagen guardada en: " + output.getAbsolutePath());

            double elapsedTimeSec = (endTime - startTime) / 1_000_000_000.0;
            System.out.printf("⏱️ Tiempo total de aplicación del filtro (2 hilos): %.3f segundos%n", elapsedTimeSec);

        } catch (Exception e) {
            System.out.println("❌ Error al aplicar el filtro: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 📘 Genera un kernel gaussiano dinámico.
     * @param size tamaño del kernel (impar)
     * @param sigma desviación estándar
     * @return matriz normalizada de tipo float[]
     */
    public static float[] createGaussianKernel(int size, double sigma) {
        float[] kernel = new float[size * size];
        double mean = size / 2;
        double sum = 0.0; // para normalizar los valores

        for (int x = 0; x < size; x++) {
            for (int y = 0; y < size; y++) {
                double exponent = -0.5 * (Math.pow((x - mean) / sigma, 2.0) + Math.pow((y - mean) / sigma, 2.0));
                double value = Math.exp(exponent);
                kernel[y * size + x] = (float) value;
                sum += value;
            }
        }

        // Normalizar el kernel para que la suma sea 1
        for (int i = 0; i < kernel.length; i++) {
            kernel[i] /= sum;
        }

        return kernel;
    }
}
