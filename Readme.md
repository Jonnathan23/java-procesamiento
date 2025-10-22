# Comparación de Multiplicación de matrices
## 📊 Análisis de rendimiento — Multiplicación de matrices en la nube

### Análisis comparativo de tiempos

El experimento evaluó la **multiplicación de matrices cuadradas** en un entorno de cómputo en la nube, comparando dos modos de ejecución:

- **Sin hilos:** ejecución secuencial o monohilo.  
- **Con hilos (máximo permitido):** ejecución paralela aprovechando múltiples núcleos.

| Dimensión | Sin hilos (s) | Con hilos (s) | Diferencia (s) | Mejora (%) |
|------------|----------------|----------------|----------------|-------------|
| 1000×1000  | 1.591 | 1.634 | -0.043 | -2.7 % |
| 2000×2000  | 66.499 | 61.955 | 4.544 | 6.8 % |
| 3000×3000  | 292.986 | 284.145 | 8.841 | 3.0 % |

<img src="img/multiplicacion_matrices_cloud_barras.png" alt="Imagen Comparativa" width="200">

**Interpretación:**  
- En matrices pequeñas (1000×1000), la sobrecarga de creación y sincronización de hilos supera el beneficio del paralelismo, resultando en un tiempo ligeramente peor.  
- Para tamaños intermedios y grandes (2000×2000 y 3000×3000), se observan **reducciones del tiempo total entre 3 % y 7 %**, lo que demuestra una mejora moderada.  
- El rendimiento no escala linealmente, lo cual indica limitaciones asociadas a **ancho de banda de memoria** o **sincronización entre hilos**.  

### Conclusión general

El uso de hilos en la nube mejora el rendimiento de la multiplicación de matrices **solo cuando el tamaño del problema es lo suficientemente grande** para compensar el costo de paralelizar.  
En tareas pequeñas, el manejo de concurrencia introduce una sobrecarga que puede incluso **aumentar el tiempo de ejecución**.  

Esto evidencia que el **beneficio del paralelismo depende directamente del tamaño de la carga de trabajo y de la arquitectura del entorno de ejecución** (núcleos disponibles, políticas de CPU virtuales y recursos compartidos en la nube).  


---

# Comparación de Rendimiento Filtro Gauss

## Análisis de rendimiento del filtro Gaussiano (monohilo vs multihilo)

### Datos base

| Nº de hilos | Tiempo (s) | Speedup (S = T₁ / Tₙ) | Mejora (%) | Eficiencia (E = S / Nº hilos) |
|--------------|-------------|----------------------|-------------|-------------------------------|
| 1 hilo       | 24.586      | 1.00                | —           | 1.000                         |
| 2 hilos      | 11.995      | 2.05                | **51.2%**   | 1.025                         |
| 5 hilos      | 4.535       | 5.42                | **81.6%**   | 1.084                         |


### Análisis

- El paso de **1 a 2 hilos** reduce el tiempo de ejecución de `24.586 s` a `11.995 s`, lo que representa una **mejora del 51%**.  
  El programa casi **duplica su velocidad**, mostrando un buen aprovechamiento del paralelismo.

- Con **5 hilos**, el tiempo baja drásticamente a `4.535 s`, alcanzando una **mejora del 81.6%** y un **speedup de 5.42×**, lo que indica una **escalabilidad casi lineal**.

- La **eficiencia** se mantiene por encima de 1.0, lo cual sugiere un **superescalamiento leve**, posiblemente debido al uso efectivo de la caché y la planificación del sistema operativo.

<img src="img/comparativa_filtro_gauss.png" alt="Imagen Comparativa" width="200">

---

### Conclusión

El experimento demuestra que el **uso de múltiples hilos mejora significativamente el rendimiento** del filtro Gaussiano:

- Se reduce el tiempo total de **24.586 s a 4.535 s** (≈ **81.6% menos tiempo**).  
- El procesamiento **paralelo** aprovecha de forma óptima los recursos del procesador.  
- El sistema muestra una **escalabilidad casi ideal**, sin pérdida de eficiencia por sobrecarga.

> 💡 **Conclusión general:**  
> El procesamiento multihilo ofrece un **aumento sustancial del rendimiento** y demuestra ser altamente efectivo para tareas intensivas como el filtrado Gaussiano, reduciendo considerablemente el tiempo de ejecución frente a la versión monohilo.