# AppBiblioteca

## **Requisitos**

- Android Studio 4.0 o superior
- Arquitectura x64
- PostgreSQL (o MySQL)
- Git bash o Winrar para descargar el repositorio
- SDK independiente o por default de Android Studio
- Drivers Device para Android (en caso de usar emuladores)
- Java RE

## **Instalación**

1. Clone el repositorio con git bash o descargue el .zip.
2. Dentro del directorio donde se descargó el repositorio, cree un archivo local.properties.
3. Descargue e instale [**Universal ADB Drivers**](https://adb.clockworkmod.com/) para instalar los drivers para android.

## **Ejecución**

1. Agrege dentro de local.properties la direcion absoluta del SDK, ejemplo:

    sdk.dir=C\:\\\Users\\\User\\\AppData\\\Local\\\Android\\\Sdk

2. Abra **Android Studio** y busque el proyecto.
3. Ejecute usando un emulador o conectando su dispositivo Android

    Nota: Para usar su dispositivo Android debe tener habilitado el **Modo desarrollador**, luego habilitar **Depuracion por USB**. Se recomienda usar cable USB 3.0

4. Espere a que el compilador y depurador del IDE de Android Studio se ejecute y lance la aplicación en el dispositivo.
5. Listo, disfrute de la aplicación.
