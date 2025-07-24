# CuidémonosAQP - Aplicación de Seguridad Ciudadana

## Descripción

CuidémonosAQP es una aplicación móvil de seguridad ciudadana centrada en la participación comunitaria. Está diseñada para que ciudadanos verificados puedan crear y validar puntos seguros dentro de un rango geográfico restringido, fomentando la vigilancia vecinal descentralizada.

## Funcionalidades Principales

### 1. Autenticación y Validación de Identidad
Los usuarios deben registrarse proporcionando:
- Información personal (DNI, correo, dirección, teléfono)
- Fotografía personal
- Imágenes del DNI (anverso y reverso)

### 2. Creación de Puntos Seguros con Validación Comunitaria
- **Ubicación restringida**: Los puntos solo se pueden crear dentro de un radio de 100 metros desde la ubicación GPS actual
- **Formulario completo**: Nombre del lugar, categoría, descripción, justificación e imagen representativa
- **Validación colectiva**: Requiere al menos 3 vigilantes (creador + 2 invitados) para activar la zona

### 3. Mapa Interactivo con Visualización Geolocalizada
- Visualización de puntos seguros activos
- Detalles completos al seleccionar puntos
- Clasificación visual por íconos y colores según estado

### 4. Sistema de Reputación y Comentarios
- Calificación promedio por estrellas (1-5)
- Comentarios de otros usuarios

### 5. Gestión de Invitaciones
- Sistema automático de activación de puntos seguros
- Validación colectiva por parte de la comunidad

## Arquitectura del Proyecto

### Android - MVVM Modular + Clean Architecture

Cada módulo (auth, safezone, user, profile, etc.) se estructura en tres capas:

**Presentation (UI)**
- Jetpack Compose + ViewModel
- Estados, eventos y navegación

**Domain** 
- Casos de uso (UseCase)
- Modelos de negocio y contratos
- Independiente de frameworks

**Data**
- DTOs, Retrofit APIs
- Repositorios implementados
- Mapeadores y lógica de conexión

**Tecnologías adicionales:**
- Inyección de dependencias con Hilt
- Manejo reactivo con StateFlow
- TokenManager, SessionCache, NetworkResult

###  Backend - Node.js + Express + Supabase

- **Servidor**: Node.js con Express
- **Base de datos**: PostgreSQL con Sequelize ORM
- **Seguridad**: JWT, autenticación middleware, cifrado bcrypt
- **Almacenamiento**: Supabase Storage para archivos
- **API**: Endpoints REST organizados por entidad


## Requisitos Previos

- Android Studio (versión recomendada: última estable)
- JDK 11 o superior
- Dispositivo Android o emulador configurado
- Conexión a internet

## Configuración del Proyecto

### Build Variants

El proyecto cuenta con dos configuraciones de build:

- **Debug**: Configurado para desarrollo local
- **Release**: Configurado para producción con servidor en la nube

### URLs de API por Entorno

- **Release**: `https://cuidemonosaqp-backend.onrender.com`
- **Debug**: `http://10.0.2.2:3000/` (para emulador Android)


## Instrucciones de Ejecución

### Para Ejecutar en Producción

1. **Cambiar Build Variant a Release**:
   - En Android Studio, ve a la ventana "Build Variants" (generalmente en la barra lateral izquierda)
   - Si no está visible, ve a `Build > Select Build Variant `
   - Selecciona **"release"** en el dropdown del Build Variant

2. **Ejecutar la aplicación**:
   - Conecta tu dispositivo Android o inicia un emulador
   - Presiona el botón "Run" 

### Para Ejecutar en Desarrollo (Es necesario tener el backend en el computador)

1. **Cambiar Build Variant a Debug**:
   - En la ventana "Build Variants", selecciona **"debug"**

2. **Configurar servidor local**:
   - Asegúrate de tener el servidor backend ejecutándose localmente en el puerto 3000
   - Para emulador: La URL `http://10.0.2.2:3000/` mapea al `localhost:3000` de tu máquina

3. **Ejecutar la aplicación**:
   - Ejecuta normalmente desde Android Studio

## Consideraciones Importantes

### Servidor en Render (Producción)

⚠️ **Importante**: El servidor backend en Render utiliza un plan gratuito, por lo que:

- El servidor puede estar "dormido" si no ha recibido requests recientemente
- **Primera conexión**: Puede tardar entre 30-60 segundos en responder mientras el servidor se inicia
- **Reintentos**: Si la primera request falla, espera unos momentos y vuelve a intentar
- **Paciencia requerida**: Es normal que las primeras interacciones sean lentas


## Solución de Problemas

### Error de Conexión
- Verifica que el Build Variant esté configurado correctamente
- En modo release: Espera a que el servidor de Render se active (puede tomar hasta 1 minuto)
- En modo debug: Verifica que tu servidor local esté ejecutándose
