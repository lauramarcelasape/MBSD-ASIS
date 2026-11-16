# 🏥 Master Salud - Gestión de Pacientes con Firebase

Aplicación nativa desarrollada en **Android (Java)** para la práctica **PR5 de 202627-Aplicaciones para Sistemas de Información Sanitaria y Dispositivos Móviles**. Implementa operaciones CRUD completas (Crear, Leer, Actualizar y Eliminar) conectadas en tiempo real a **Firebase Realtime Database**.

---

## 📋 Características principales

* **Inserción de pacientes (`MainActivity`):** Genera registros de pacientes con datos aleatorios (Nombre, Apellidos, Grupo Sanguíneo y código identificador `NUHSA`) y los almacena simultáneamente bajo dos nodos en la base de datos:
  * `Pacientes/<NUHSA>`
  * `<GrupoSanguineo>/<NUHSA>`
* **Búsqueda y edición (`BuscaActivity`):** Consulta un paciente por su código `NUHSA` mediante `ValueEventListener` y permite:
  * **Actualizar** su nombre y apellidos manteniendo sincronizadas ambas ramas de la base de datos.
  * **Eliminar** el registro completo del paciente en Firebase.
* **Listado en tiempo real (`ListaPacientesActivity`):** Muestra todos los pacientes almacenados en el nodo `Pacientes` mediante un `ListView` conectado en tiempo real.

---

## 🛠️ Tecnologías utilizadas

* **Lenguaje:** Java 11
* **SDK Android:** Compile SDK 37 | Min SDK 24 | Target SDK 37
* **Base de datos en la nube:** Firebase Realtime Database (`com.google.firebase:firebase-database`)
* **Interfaz de usuario:** XML Layouts (`ConstraintLayout`, `ListView`, Material Components)

---

## 📂 Estructura del proyecto

```text
app/src/main/
├── java/com/mibesb/mastersalud/
│   ├── MainActivity.java             # Pantalla principal: inserción y navegación
│   ├── BuscaActivity.java            # Consulta, edición y borrado por NUHSA
│   ├── ListaPacientesActivity.java   # Listado completo en tiempo real
│   ├── Paciente.java                 # Modelo POJO compatible con Firebase
│   └── Constantes.java               # Constantes de rutas y datos de prueba
└── res/layout/
    ├── activity_main.xml
    ├── activity_busca.xml
    └── activity_lista_pacientes.xml
```

---

## 🚀 Instalación y configuración

1. **Clonar el repositorio:**
   ```bash
   git clone https://github.com/tu-usuario/master-salud.git
   cd master-salud
   ```

2. **Configurar Firebase:**
   * Crea un proyecto en la [Consola de Firebase](https://console.firebase.google.com/).
   * Registra una aplicación Android con el nombre de paquete `com.mibesb.mastersalud`.
   * Activa **Realtime Database** en modo de prueba (o configura tus reglas de lectura/escritura).
   * Descarga el archivo `google-services.json` y colócalo dentro de la carpeta `app/`.

3. **Compilar y ejecutar:**
   * Abre el proyecto en **Android Studio**.
   * Sincroniza los archivos Gradle (`Sync Project with Gradle Files`).
   * Ejecuta la aplicación en un emulador o dispositivo físico con acceso a Internet.

---

## 📄 Licencia

Proyecto desarrollado con fines académicos para la asignatura de 202627-Aplicaciones para Sistemas de Información Sanitaria y Dispositivos Móviles.
