cmake_minimum_required(VERSION 3.10.2)

project("dragonsound")

# Carga las librerías nativas de audio del sistema Android en el servidor de Expo
find_library(log-lib log)
find_library(OpenSLES-lib OpenSLES)

# Toma el archivo sound.cpp que ya tienes guardado en la raíz de tu GitHub
add_library(sound SHARED sound.cpp)

# Enlaza los filtros de audio al motor nativo de la aplicación
target_link_libraries(sound ${OpenSLES-lib} ${log-lib})
