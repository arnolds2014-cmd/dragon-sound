cmake_minimum_required(VERSION 3.22.1)

project("audifono_propietario")

# Incluir cabeceras de Android NDK
find_library(log-lib log)

# Archivos fuente
add_library(${PROJECT_NAME} SHARED
    audio_processor.cpp
)

# Enlazar bibliotecas
target_link_libraries(${PROJECT_NAME}
    ${log-lib}
    android
)

# Incluir directorios
target_include_directories(${PROJECT_NAME} PRIVATE
    ${CMAKE_CURRENT_SOURCE_DIR}
)
