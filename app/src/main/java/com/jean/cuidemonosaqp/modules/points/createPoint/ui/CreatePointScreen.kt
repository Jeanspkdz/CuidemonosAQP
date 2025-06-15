package com.jean.cuidemonosaqp.modules.points.createPoint.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jean.cuidemonosaqp.shared.components.CurrentLocationMap

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreatePointScreen(modifier: Modifier = Modifier) {
    var nombrePunto by remember { mutableStateOf("") }
    var categoriaSeleccionada by remember { mutableStateOf("Seleccionar categoría") }
    var descripcion by remember { mutableStateOf("") }
    var porQueSeguro by remember { mutableStateOf("") }
    var vigilantesRequeridos by remember { mutableIntStateOf(1) }
    var showCategoryDropdown by remember { mutableStateOf(false) }
    var confirmarInfo by remember { mutableStateOf(false) }

    val categorias = listOf(
        "Hogar", "Trabajo", "Escuela", "Hospital",
        "Comisaría", "Estación", "Centro Comercial", "Otro"
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        // Top Bar
        TopAppBar(
            title = {
                Text(
                    "Crear Punto Seguro",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Medium
                )
            },
            navigationIcon = {
                IconButton(onClick = { /* Navigate back */ }) {
                    Icon(Icons.Default.Close, contentDescription = "Cerrar")
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = Color.White
            )
        )

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            item {
                // Mapa
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color(0xFFE8F5E8))
                        .border(1.dp, Color(0xFFE0E0E0), RoundedCornerShape(12.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        CurrentLocationMap()
                    }
                }
            }

            item {
                // Nombre del punto seguro
                Column {
                    Text(
                        "Nombre del punto seguro *",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color.Black
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = nombrePunto,
                        onValueChange = { nombrePunto = it },
                        placeholder = { Text("Casa Principal", color = Color.Gray) },
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color(0xFF4CAF50),
                            unfocusedBorderColor = Color(0xFFE0E0E0)
                        )
                    )
                }
            }

            item {
                // Categoría
                Column {
                    Text(
                        "Categoría *",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color.Black
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    ExposedDropdownMenuBox(
                        expanded = showCategoryDropdown,
                        onExpandedChange = { showCategoryDropdown = !showCategoryDropdown }
                    ) {
                        OutlinedTextField(
                            value = categoriaSeleccionada,
                            onValueChange = { },
                            readOnly = true,
                            trailingIcon = {
                                ExposedDropdownMenuDefaults.TrailingIcon(
                                    expanded = showCategoryDropdown
                                )
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .menuAnchor(),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = Color(0xFF4CAF50),
                                unfocusedBorderColor = Color(0xFFE0E0E0)
                            )
                        )
                        ExposedDropdownMenu(
                            expanded = showCategoryDropdown,
                            onDismissRequest = { showCategoryDropdown = false }
                        ) {
                            categorias.forEach { categoria ->
                                DropdownMenuItem(
                                    text = { Text(categoria) },
                                    onClick = {
                                        categoriaSeleccionada = categoria
                                        showCategoryDropdown = false
                                    }
                                )
                            }
                        }
                    }
                }
            }

            item {
                // Descripción breve
                Column {
                    Text(
                        "Descripción breve",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color.Black
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = descripcion,
                        onValueChange = { descripcion = it },
                        placeholder = { Text("Describe este punto seguro...", color = Color.Gray) },
                        modifier = Modifier.fillMaxWidth(),
                        minLines = 3,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color(0xFF4CAF50),
                            unfocusedBorderColor = Color(0xFFE0E0E0)
                        )
                    )
                }
            }

            item {
                // ¿Por qué es seguro este punto?
                Column {
                    Text(
                        "¿Por qué es seguro este punto? *",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color.Black
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = porQueSeguro,
                        onValueChange = { porQueSeguro = it },
                        placeholder = { Text("Explica por qué consideras que este es un punto seguro...", color = Color.Gray) },
                        modifier = Modifier.fillMaxWidth(),
                        minLines = 3,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color(0xFF4CAF50),
                            unfocusedBorderColor = Color(0xFFE0E0E0)
                        )
                    )
                }
            }

            item {
                // Vigilantes requeridos
                Column {
                    Text(
                        "Vigilantes requeridos",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color.Black
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        "Selecciona mínimo 1 vigilante para activar este punto seguro",
                        fontSize = 14.sp,
                        color = Color.Gray
                    )
                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        repeat(3) { index ->
                            val numero = index + 1
                            Box(
                                modifier = Modifier
                                    .size(40.dp)
                                    .clip(CircleShape)
                                    .background(
                                        if (vigilantesRequeridos >= numero)
                                            Color(0xFF4CAF50)
                                        else
                                            Color(0xFFF0F0F0)
                                    )
                                    .clickable { vigilantesRequeridos = numero },
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    numero.toString(),
                                    color = if (vigilantesRequeridos >= numero)
                                        Color.White
                                    else
                                        Color.Gray,
                                    fontWeight = FontWeight.Medium
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            Icons.Default.Add,
                            contentDescription = null,
                            tint = Color.Gray,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            "Invitar vigilantes por nombre o correo",
                            color = Color.Gray,
                            fontSize = 14.sp
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Usuario ejemplo
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .clip(CircleShape)
                                .background(Color(0xFF4CAF50)),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                "CR",
                                color = Color.White,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Medium
                            )
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(
                            "Carlos Rodríguez",
                            fontSize = 16.sp,
                            color = Color.Black,
                            modifier = Modifier.weight(1f)
                        )
                        IconButton(onClick = { /* Remove user */ }) {
                            Icon(
                                Icons.Default.Delete,
                                contentDescription = "Eliminar",
                                tint = Color.Gray
                            )
                        }
                    }
                }
            }

//            item {
//                // Añadir evidencia de seguridad
//                Column {
//                    Text(
//                        "Añadir evidencia de seguridad",
//                        fontSize = 16.sp,
//                        fontWeight = FontWeight.Medium,
//                        color = Color.Black
//                    )
//                    Spacer(modifier = Modifier.height(12.dp))
//
//                    Row(
//                        horizontalArrangement = Arrangement.spacedBy(16.dp)
//                    ) {
//                        // Tomar foto
//                        Column(
//                            horizontalAlignment = Alignment.CenterHorizontally,
//                            modifier = Modifier
//                                .clickable { /* Take photo */ }
//                        ) {
//                            Box(
//                                modifier = Modifier
//                                    .size(60.dp)
//                                    .clip(RoundedCornerShape(12.dp))
//                                    .background(Color(0xFFE3F2FD)),
//                                contentAlignment = Alignment.Center
//                            ) {
//                                Icon(
//                                    Icons.Default.Phone,
//                                    contentDescription = null,
//                                    tint = Color(0xFF2196F3),
//                                    modifier = Modifier.size(24.dp)
//                                )
//                            }
//                            Spacer(modifier = Modifier.height(4.dp))
//                            Text(
//                                "Tomar foto",
//                                fontSize = 12.sp,
//                                color = Color.Gray,
//                                textAlign = TextAlign.Center
//                            )
//                        }
//
//                        // Subir foto
//                        Column(
//                            horizontalAlignment = Alignment.CenterHorizontally,
//                            modifier = Modifier
//                                .clickable { /* Upload photo */ }
//                        ) {
//                            Box(
//                                modifier = Modifier
//                                    .size(60.dp)
//                                    .clip(RoundedCornerShape(12.dp))
//                                    .background(Color(0xFFE3F2FD)),
//                                contentAlignment = Alignment.Center
//                            ) {
//                                Icon(
//                                    Icons.Default.Face,
//                                    contentDescription = null,
//                                    tint = Color(0xFF2196F3),
//                                    modifier = Modifier.size(24.dp)
//                                )
//                            }
//                            Spacer(modifier = Modifier.height(4.dp))
//                            Text(
//                                "Subir foto",
//                                fontSize = 12.sp,
//                                color = Color.Gray,
//                                textAlign = TextAlign.Center
//                            )
//                        }
//
//                        // Transmisión
//                        Column(
//                            horizontalAlignment = Alignment.CenterHorizontally,
//                            modifier = Modifier
//                                .clickable { /* Start transmission */ }
//                        ) {
//                            Box(
//                                modifier = Modifier
//                                    .size(60.dp)
//                                    .clip(RoundedCornerShape(12.dp))
//                                    .background(Color(0xFFE3F2FD)),
//                                contentAlignment = Alignment.Center
//                            ) {
//                                Icon(
//                                    Icons.Default.Share,
//                                    contentDescription = null,
//                                    tint = Color(0xFF2196F3),
//                                    modifier = Modifier.size(24.dp)
//                                )
//                            }
//                            Spacer(modifier = Modifier.height(4.dp))
//                            Text(
//                                "Transmisión",
//                                fontSize = 12.sp,
//                                color = Color.Gray,
//                                textAlign = TextAlign.Center
//                            )
//                        }
//                    }
//                }
//            }

            item {
                // Checkbox de confirmación
                Row(
                    verticalAlignment = Alignment.Top,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Checkbox(
                        checked = confirmarInfo,
                        onCheckedChange = { confirmarInfo = it },
                        colors = CheckboxDefaults.colors(
                            checkedColor = Color(0xFF4CAF50)
                        )
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        "Confirmo que la información proporcionada es precisa y asumo la responsabilidad por la creación de este punto seguro",
                        fontSize = 14.sp,
                        color = Color.Gray,
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            item {
                // Advertencia
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            Color(0xFFFFF3E0),
                            RoundedCornerShape(8.dp)
                        )
                        .padding(12.dp)
                ) {
                    Icon(
                        Icons.Default.Warning,
                        contentDescription = null,
                        tint = Color(0xFFFF9800),
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        "El punto necesitará ser verificado por los vigilantes invitados antes de que esté activo",
                        fontSize = 13.sp,
                        color = Color(0xFF795548),
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            item {
                // Botón crear punto seguro
                Button(
                    onClick = { /* Create safe point */ },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF4CAF50)
                    ),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(
                        "Crear Punto Seguro",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            item {
                Spacer(modifier = Modifier.height(20.dp))
            }
        }
    }
}