import java.awt.image.BufferedImage;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import javax.imageio.ImageIO;

public class GaussianParallel5 {
    public static void main(String[] args) {
        try {
            String pathImage = "C:/Users/Jonna/Desktop/ups/8ctavo ciclo/paralelo/Filtros_java/imgs/digital_art.jpg";

            BufferedImage image = ImageIO.read(new File(pathImage));

            int kernelSize = 51; // Tamaño del kernel
            double sigma = 15; // Desviación estándar (intensidad del desenfoque)
            int numThreads = 5; // Número de hilos

            // Crear el kernel gaussiano
            float[] matrix = createGaussianKernel(kernelSize, sigma);
            Kernel kernel = new Kernel(kernelSize, kernelSize, matrix);
            ConvolveOp op = new ConvolveOp(kernel, ConvolveOp.EDGE_NO_OP, null);

            int width = image.getWidth();
            int height = image.getHeight();
            int segmentHeight = height / numThreads;

            // Crear un pool de 5 hilos
            ExecutorService executor = Executors.newFixedThreadPool(numThreads);
            List<Future<BufferedImage>> results = new ArrayList<>();

            System.out.println("🚀 Aplicando filtro gaussiano con " + numThreads + " hilos...");
            long startTime = System.nanoTime();

            // Enviar tareas (una por cada hilo)
            for (int i = 0; i < numThreads; i++) {
                int startY = i * segmentHeight;
                int currentHeight = (i == numThreads - 1) ? height - startY : segmentHeight;

                BufferedImage segment = image.getSubimage(0, startY, width, currentHeight);

                Callable<BufferedImage> task = () -> {
                    BufferedImage resultSegment = new BufferedImage(width, currentHeight, image.getType());
                    op.filter(segment, resultSegment);
                    return resultSegment;
                };

                results.add(executor.submit(task));
            }

            // Esperar los resultados de todos los hilos
            BufferedImage finalImage = new BufferedImage(width, height, image.getType());
            int currentY = 0;

            for (Future<BufferedImage> future : results) {
                BufferedImage part = future.get();
                finalImage.createGraphics().drawImage(part, 0, currentY, null);
                currentY += part.getHeight();
            }

            long endTime = System.nanoTime();
            executor.shutdown();

            //Guardar imagen resultante
            File output = new File(
                    "C:/Users/Jonna/Desktop/ups/8ctavo ciclo/paralelo/Filtros_java/imgs/digital_art_blur_5threads.jpg");
            ImageIO.write(finalImage, "jpg", output);

            // Mostrar resultados
            double elapsedTimeSec = (endTime - startTime) / 1_000_000_000.0;
            System.out.println("Filtro gaussiano aplicado con éxito en paralelo.");
            System.out.printf(" Tiempo total de ejecución con %d hilos: %.3f segundos%n", numThreads, elapsedTimeSec);
            System.out.println(" Imagen guardada en: " + output.getAbsolutePath());

        } catch (Exception e) {
            System.out.println("Error al aplicar el filtro: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Genera un kernel gaussiano dinámico.
     * 
     * @param size  tamaño del kernel (impar)
     * @param sigma desviación estándar
     * @return matriz normalizada de tipo float[]
     */
    public static float[] createGaussianKernel(int size, double sigma) {
        float[] kernel = new float[size * size];
        double mean = size / 2.0;
        double sum = 0.0; // para normalizar los valores

        for (int x = 0; x < size; x++) {
            for (int y = 0; y < size; y++) {
                double exponent = -0.5 * (Math.pow((x - mean) / sigma, 2.0) + Math.pow((y - mean) / sigma, 2.0));
                double value = Math.exp(exponent);
                kernel[y * size + x] = (float) value;
                sum += value;
            }
        }

        // Normalizar el kernel
        for (int i = 0; i < kernel.length; i++) {
            kernel[i] /= sum;
        }

        return kernel;
    }
}
