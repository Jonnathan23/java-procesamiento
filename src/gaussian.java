import java.awt.image.BufferedImage;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;
import java.io.File;
import javax.imageio.ImageIO;

public class gaussian {
    public static void main(String[] args) {
        try {
            // 📁 Ruta de la imagen original
            String pathImage = "C:/Users/Jonna/Desktop/ups/8ctavo ciclo/paralelo/Filtros_java/imgs/digital_art.jpg";

            // 🖼️ Cargar la imagen
            BufferedImage image = ImageIO.read(new File(pathImage));

            // 🔧 Parámetros configurables
            int kernelSize = 51; // Tamaño del kernel (debe ser impar: 3, 5, 7, 9...)
            double sigma = 15; // Desviación estándar (controla la intensidad del desenfoque)

            // 🧮 Generar el kernel gaussiano
            float[] matrix = createGaussianKernel(kernelSize, sigma);

            // 📦 Crear y aplicar el filtro
            Kernel kernel = new Kernel(kernelSize, kernelSize, matrix);
            ConvolveOp op = new ConvolveOp(kernel, ConvolveOp.EDGE_NO_OP, null);
            BufferedImage blurred = op.filter(image, null);

            // 💾 Guardar la imagen resultante
            File output = new File(
                    "C:/Users/Jonna/Desktop/ups/8ctavo ciclo/paralelo/Filtros_java/imgs/digital_art_blur_dynamic.jpg");
            ImageIO.write(blurred, "jpg", output);

            System.out.println("✅ Filtro gaussiano aplicado con éxito.");
            System.out.println("Imagen guardada en: " + output.getAbsolutePath());

        } catch (Exception e) {
            System.out.println("❌ Error al aplicar el filtro: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // 📘 Función para generar un kernel gaussiano dinámico
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
