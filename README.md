# MetaMapa

## Descripción del proyecto
MetaMapa es una aplicación tipo red social orientada a la publicación y visualización de hechos geolocalizados.  
Permite a los usuarios registrar eventos, incidentes o reportes ciudadanos, analizarlos mediante filtros, estadísticas y reportes, y visualizarlos sobre un mapa interactivo.

El sistema fue diseñado siguiendo una arquitectura basada en microservicios, con frontend y backend desacoplados, APIs REST y despliegue en la nube utilizando AWS.

## Funcionalidades

### Gestión de usuarios
- Registro e inicio de sesión de usuarios.
- Autenticación y autorización para funcionalidades protegidas.
- Asociación de hechos y reportes a usuarios registrados.

### Publicación de hechos
- Creación, edición y eliminación de hechos.
- Carga de información estructurada (título, descripción, categoría, fecha y ubicación).
- Geolocalización de hechos para su visualización en el mapa.
- Soporte para distintos tipos de hechos (eventos, incidentes, reportes ciudadanos).

### Reportes
- Generación de reportes a partir de los hechos cargados.
- Visualización consolidada por categoría, zona geográfica y período de tiempo.
- Consulta y análisis de reportes.

### Filtros y búsqueda
- Filtrado por categoría, rango de fechas, ubicación geográfica y usuario.
- Búsqueda mediante texto libre.
- Combinación de múltiples filtros para análisis más precisos.

### Visualización en mapa
- Mapa interactivo con marcadores geolocalizados.
- Visualización dinámica de hechos según filtros aplicados.
- Agrupamiento de marcadores en zonas de alta densidad.
- Acceso al detalle del hecho desde el mapa.

### Estadísticas
- Visualización de estadísticas generales del sistema.
- Métricas por categoría, ubicación y período.
- Gráficos y resúmenes para identificación de patrones y tendencias.
- Soporte para análisis de datos históricos.

### Arquitectura
- Arquitectura basada en microservicios.
- Separación clara entre frontend y backend.
- Backend expuesto mediante APIs REST.
- Servicios desplegados en AWS, preparados para escalar y evolucionar de forma independiente.

### Seguridad y buenas prácticas
- Validación de datos en frontend y backend.
- Manejo de errores y respuestas HTTP estandarizadas.
- Diseño orientado a la mantenibilidad y extensibilidad del sistema.

---

# MetaMapa (English Version)

## Project Description
MetaMapa is a social-style application focused on publishing and visualizing geolocated events.  
It allows users to register events, incidents, or citizen reports, analyze them using filters, statistics, and reports, and visualize them on an interactive map.

The system was designed following a microservices-based architecture, with decoupled frontend and backend, REST APIs, and cloud deployment on AWS.

## Features

### User management
- User registration and authentication.
- Authorization for protected features.
- Association of events and reports with registered users.

### Event publishing
- Creation, editing, and deletion of events.
- Structured data input (title, description, category, date, and location).
- Event geolocation for map visualization.
- Support for different event types (events, incidents, citizen reports).

### Reports
- Report generation based on published events.
- Aggregated visualization by category, geographic area, and time period.
- Report consultation and analysis.

### Filters and search
- Filtering by category, date range, geographic location, and user.
- Free-text search.
- Combination of multiple filters for refined analysis.

### Map visualization
- Interactive map with geolocated markers.
- Dynamic visualization based on applied filters.
- Marker clustering in high-density areas.
- Access to event details directly from the map.

### Statistics
- General system statistics.
- Metrics by category, location, and time period.
- Charts and summaries to identify patterns and trends.
- Support for historical data analysis.

### Architecture
- Microservices-based architecture.
- Clear separation between frontend and backend.
- Backend exposed through REST APIs.
- Services deployed on AWS, designed for scalability and independent evolution.

### Security and best practices
- Data validation on frontend and backend.
- Standardized HTTP error handling.
- Design focused on maintainability and extensibility.
