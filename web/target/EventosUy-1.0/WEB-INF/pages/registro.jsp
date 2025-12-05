<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta http-equiv="Cache-Control" content="no-cache, no-store, must-revalidate" />
    <meta http-equiv="Pragma" content="no-cache" />
    <meta http-equiv="Expires" content="0" />
    <title>Registrarse - Eventos.uy</title>
    
    <link rel="stylesheet" href="<%=request.getContextPath()%>/assets/css/index.css">
    <link rel="stylesheet" href="https://fonts.googleapis.com/css2?family=Inter&display=swap" />
    <link rel="icon" type="image/x-icon" href="<%=request.getContextPath()%>/assets/icons/Logo.png">
    
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="<%=request.getContextPath()%>/assets/css/ConsultaEvento.css">
    <link rel="stylesheet" href="<%=request.getContextPath()%>/assets/css/styles.css">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdn.jsdelivr.net/npm/boxicons@latest/css/boxicons.min.css" rel="stylesheet">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Titillium+Web:wght@300;400;600&display=swap" rel="stylesheet">
    
    <!-- Estilos simples para mensajes de disponibilidad -->
    <style>
        .availability-available {
            color: #198754 !important;
            font-weight: bold;
        }
        
        .availability-unavailable {
            color: #dc3545 !important;
            font-weight: bold;
        }
    </style>
</head>

<jsp:include page="../templates/header.jsp"></jsp:include>

<body>
    <div class="container d-flex justify-content-center">
        
        <!-- Contenedor del formulario -->
        <div class="col-md-5 col-lg-7 col-xxl-9 mt-3 mt-sm-3 mt-md-0 Registro base text-center">
            <h1 class="mb-5">
                <strong></strong>
            </h1>
            <h3 class="mb-4">
                <strong>Registrarse</strong>
            </h3>

            <!-- Mostrar errores si existen -->
            <% if (request.getAttribute("error") != null) { %>
                <div class="alert alert-danger" role="alert">
                    <%= request.getAttribute("error") %>
                </div>
            <% } %>

            <p class="mb-3">Introduce tus datos de usuario para crear una cuenta nueva</p>
            
            <form id="registroForm" action="<%=request.getContextPath()%>/registro" method="post" class="needs-validation" autocomplete="off" enctype="multipart/form-data">
                
                <!-- Campos básicos -->
                <div class="mb-3">
                    <input type="text" class="form-control" id="nickname" name="nickname" 
                           placeholder="Apodo *" 
                           value="<%= request.getAttribute("nickname") != null ? request.getAttribute("nickname") : "" %>" 
                           required autocomplete="off"
                           oninvalid="this.setCustomValidity('Por favor ingrese un apodo.')"
                           oninput="this.setCustomValidity(''); verificarNickname()">
                    <div class="invalid-feedback">
                        Por favor ingrese un apodo.
                    </div>
                    <small id="nicknameStatus" class="text-muted"></small>
                </div>
                
                <div class="mb-3">
                    <input type="text" class="form-control" id="nombre" name="nombre" 
                           placeholder="Nombre *" 
                           value="<%= request.getAttribute("nombre") != null ? request.getAttribute("nombre") : "" %>" 
                           required autocomplete="off"
                           oninvalid="this.setCustomValidity('Por favor ingrese su nombre.')"
                           oninput="this.setCustomValidity('')">
                    <div class="invalid-feedback">
                        Por favor ingrese su nombre.
                    </div>
                </div>
                
                <div class="mb-3">
                    <input type="email" class="form-control" id="email" name="email" 
                           placeholder="Correo electrónico *" 
                           value="<%= request.getAttribute("email") != null ? request.getAttribute("email") : "" %>" 
                           required autocomplete="off"
                           oninvalid="if(this.validity.valueMissing) this.setCustomValidity('Por favor ingrese un correo electrónico.'); else if(this.validity.typeMismatch) this.setCustomValidity('Por favor ingrese un correo electrónico válido.');"
                           oninput="this.setCustomValidity(''); verificarEmail()">
                    <div class="invalid-feedback">
                        Por favor ingrese un correo electrónico válido.
                    </div>
                    <small id="emailStatus" class="text-muted"></small>
                </div>
                
                <div class="mb-3">
                    <input type="password" class="form-control" id="password" name="password" 
                           placeholder="Contraseña *" required autocomplete="off" minlength="6"
                           oninvalid="if(this.validity.valueMissing) this.setCustomValidity('Por favor ingrese una contraseña.'); else if(this.validity.tooShort) this.setCustomValidity('La contraseña debe tener al menos 6 caracteres.');"
                           oninput="this.setCustomValidity(''); validatePasswordMatch();">
                    <div class="invalid-feedback">
                        La contraseña debe tener al menos 6 caracteres.
                    </div>
                </div>
                
                <div class="mb-3">
                    <input type="password" class="form-control" id="confirmPassword" name="confirmPassword" 
                           placeholder="Confirmar contraseña *" required autocomplete="off" minlength="6"
                           oninvalid="if(this.validity.valueMissing) this.setCustomValidity('Por favor ingrese una contraseña.'); else if(this.validity.tooShort) this.setCustomValidity('La contraseña debe tener al menos 6 caracteres.');
                           else if(this.value != document.getElementById('password').value) this.setCustomValidity('La contraseña no coincide.');"
                           oninput="validatePasswordMatch()">
                    <div class="invalid-feedback">
                        La contraseña no coincide.
                    </div>
                </div>
                
                <div class="mb-3">
                    <input type="file" class="form-control" id="imgPerfil" name="imagen"
                           accept="image/*">
                    <small class="text-muted">Formatos admitidos: JPG, PNG, GIF. Tamaño máximo: 5MB</small>
                </div>
                
                <!-- Tipo de usuario -->
                <div class="mb-3">
                    <label class="form-label">Tipo de usuario: <span class="text-danger">*</span></label>
                    <div class="d-flex justify-content-center align-items-center gap-4">
                        <div class="form-check">
                            <input type="radio" class="form-check-input" id="tipoAsistente" name="tipoUsuario" value="asistente"
                                   <%= !"organizador".equals(request.getAttribute("tipoUsuario")) ? "checked" : "" %>
                                   required
                                   oninvalid="this.setCustomValidity('Por favor seleccione un tipo de usuario.')"
                                   onchange="this.setCustomValidity(''); toggleUserFields()">
                            <label class="form-check-label" for="tipoAsistente">
                                Asistente
                            </label>
                        </div>
                        <div class="form-check">
                            <input type="radio" class="form-check-input" id="tipoOrganizador" name="tipoUsuario" value="organizador"
                                   <%= "organizador".equals(request.getAttribute("tipoUsuario")) ? "checked" : "" %>
                                   required
                                   oninvalid="this.setCustomValidity('Por favor seleccione un tipo de usuario.')"
                                   onchange="this.setCustomValidity(''); toggleUserFields()">
                            <label class="form-check-label" for="tipoOrganizador">
                                Organizador
                            </label>
                        </div>
                    </div>
                    <div class="invalid-feedback" id="tipoInvalidFeedback">
                        Por favor seleccione un tipo de usuario.
                    </div>
                </div>

                <!-- Campos específicos para Asistente -->
                <div id="asistenteFields" style="<%= "organizador".equals(request.getAttribute("tipoUsuario")) ? "display: none;" : "" %>">
                    <div class="mb-3">
                        <input type="text" class="form-control" id="apellido" name="apellido" 
                               placeholder="Apellido *" 
                               value="<%= request.getAttribute("apellido") != null ? request.getAttribute("apellido") : "" %>" 
                               required autocomplete="off"
                               oninvalid="this.setCustomValidity('Por favor ingrese su apellido.')"
                               oninput="this.setCustomValidity('')">
                        <div class="invalid-feedback">
                            Por favor ingrese su apellido.
                        </div>
                    </div>
                    
                    <div class="mb-3">
                        <input type="date" class="form-control" id="fechaNacimiento" name="fechaNacimiento" 
                               value="<%= request.getAttribute("fechaNacimiento") != null ? request.getAttribute("fechaNacimiento") : "" %>"
                               required
                               oninvalid="this.setCustomValidity('Por favor ingrese su fecha de nacimiento.')"
                               oninput="validateFechaNacimiento(this)">
                        <small class="text-muted">Fecha de nacimiento *</small>
                        <div class="invalid-feedback" id="fechaNacimientoError">
                            Por favor ingrese una fecha de nacimiento válida.
                        </div>
                    </div>
                    
                    <!-- Selector de institución -->
                    <div class="mb-3">
                        <label for="institucionSelect" class="form-label">Si perteneces a una institución, seleccionala.</label>
                        <select class="form-select" id="institucionSelect" name="institucion" aria-label="Selecciona una institución">
                            <option value="">Selecciona una institución (opcional)</option>
                            <%
                                java.util.Set<String> instituciones = (java.util.Set<String>) request.getAttribute("instituciones");
                                String institucionSeleccionada = (String) request.getAttribute("institucion");
                                if (instituciones != null && !instituciones.isEmpty()) {
                                    for (String institucion : instituciones) {
                                        String selected = java.util.Objects.equals(institucion, institucionSeleccionada) ? " selected" : "";
                            %>
                                <option value="<%= institucion %>"<%= selected %>><%= institucion %></option>
                            <%
                                    }
                                }
                            %>
                        </select>
                    </div>
                </div>

                <!-- Campos específicos para Organizador -->
                <div id="organizadorFields" style="<%= !"organizador".equals(request.getAttribute("tipoUsuario")) ? "display: none;" : "" %>">
                    <div class="mb-3">
                        <label for="descripcion" class="form-label">Una breve descripción de su organización *</label>
                        <textarea class="form-control" name="descripcion" id="descripcion" rows="3"
                                  autocomplete="off" required oninvalid="this.setCustomValidity('Por favor ingrese una descripción.')"
                                  oninput="this.setCustomValidity('')"><%= request.getAttribute("descripcion") != null ? request.getAttribute("descripcion") : "" %></textarea>
                        <div class="invalid-feedback">
                            Por favor ingrese una descripción.
                        </div>
                    </div>
                    
                    <div class="mb-3">
                        <input type="url" class="form-control" id="sitioWeb" name="sitioWeb" 
                               placeholder="URL a su sitio web (opcional)" 
                               value="<%= request.getAttribute("sitioWeb") != null ? request.getAttribute("sitioWeb") : "" %>" 
                               autocomplete="off">
                        <small class="text-muted">Campo opcional</small>
                    </div>
                </div>

                <button type="submit" class="btn btn-dark">
                    <span id="botonTexto">Continuar</span>
                </button>
            </form>
        </div>
    </div>

    <!-- Solo Bootstrap -->
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
    <script>
    
        function toggleUserFields() {
            const tipoAsistente = document.getElementById('tipoAsistente').checked;
            const asistenteFields = document.getElementById('asistenteFields');
            const organizadorFields = document.getElementById('organizadorFields');
            
            const apellidoField = document.getElementById('apellido');
            const fechaNacimientoField = document.getElementById('fechaNacimiento');
            
            const descripcionField = document.getElementById('descripcion');
            const sitioWebField = document.getElementById('sitioWeb');
            
            if (tipoAsistente) {
                asistenteFields.style.display = 'block';
                organizadorFields.style.display = 'none';
                
                apellidoField.required = true;
                fechaNacimientoField.required = true;
                
                descripcionField.required = false;
                sitioWebField.required = false;
            } else {
                asistenteFields.style.display = 'none';
                organizadorFields.style.display = 'block';
                
                apellidoField.required = false;
                fechaNacimientoField.required = false;
                
                descripcionField.required = true;
                sitioWebField.required = false; 
            }
        }
        
        function validateFechaNacimiento(input) {
            const fechaNacimiento = new Date(input.value);
            const hoy = new Date();
            
            input.setCustomValidity('');
            document.getElementById('fechaNacimientoError').style.display = 'none';
            
            if (fechaNacimiento > hoy) {
                input.setCustomValidity('La fecha de nacimiento no puede ser en el futuro.');
                document.getElementById('fechaNacimientoError').style.display = 'block';
            }
        }
        
        document.addEventListener('DOMContentLoaded', function() {
            toggleUserFields();
        });
        
        window.addEventListener("pageshow", function(event) {
            if (event.persisted) {
                window.location.reload();
            }
        });
        
        function validatePasswordMatch() {
            const password = document.getElementById('password');
            const confirmPassword = document.getElementById('confirmPassword');
            
            confirmPassword.setCustomValidity('');
            
            if (confirmPassword.value === '') {
                return; 
            }
            
            //CHECKEAMOS QUE LAS CONTRASENIAS COINCIDAN
            if (password.value !== confirmPassword.value) {
                confirmPassword.setCustomValidity('Las contraseñas no coinciden.');
                
                confirmPassword.classList.add('is-invalid');
                confirmPassword.classList.remove('is-valid');
            } else {
                confirmPassword.setCustomValidity('');
                confirmPassword.classList.add('is-valid');
                confirmPassword.classList.remove('is-invalid');
            }
        }
		
        //actualizamos para ver si coincide o no
        document.addEventListener('DOMContentLoaded', function() {
            const password = document.getElementById('password');
            const confirmPassword = document.getElementById('confirmPassword');
            
            password.addEventListener('input', validatePasswordMatch);
            confirmPassword.addEventListener('input', validatePasswordMatch);
            
            document.getElementById('registroForm').addEventListener('submit', function(event) {
                if (password.value !== confirmPassword.value && confirmPassword.value !== '') {
                    event.preventDefault();
                    confirmPassword.setCustomValidity('Las contraseñas no coinciden.');
                    confirmPassword.classList.add('is-invalid');
                    confirmPassword.reportValidity();
                }
            });
            
            toggleUserFields();
        });
        
        function verificarNickname() {
            const nickname = document.getElementById('nickname').value;
            const status = document.getElementById('nicknameStatus');
            
        
            
            // Llamada AJAX real
            const xhr = new XMLHttpRequest();
            xhr.open('GET', '<%=request.getContextPath()%>/verificar-disponibilidad?tipo=nickname&valor=' + encodeURIComponent(nickname), true);
            xhr.onreadystatechange = function() {
                if (xhr.readyState === 4 && xhr.status === 200) {
                    try {
                        const response = JSON.parse(xhr.responseText);
                        if (response.disponible) {
                            status.textContent = 'Apodo disponible.';
                            status.className = 'availability-available';
                        } else {
                            status.textContent = 'Apodo no disponible.';
                            status.className = 'availability-unavailable';
                        }
                    } catch (e) {
                        status.textContent = 'Error al verificar disponibilidad.';
                        status.className = 'availability-unavailable';
                    }
                }
            };
            xhr.send();
        }
        
        function verificarEmail() {
            const email = document.getElementById('email').value;
            const status = document.getElementById('emailStatus');
            
            if (!validateEmail(email)) {
                status.textContent = 'Ingrese un correo electrónico válido.';
                status.className = 'text-danger availability-unavailable';
                return;
            }
            
            // Llamada AJAX real
            const xhr = new XMLHttpRequest();
            xhr.open('GET', '<%=request.getContextPath()%>/verificar-disponibilidad?tipo=email&valor=' + encodeURIComponent(email), true);
            xhr.onreadystatechange = function() {
                if (xhr.readyState === 4 && xhr.status === 200) {
                    try {
                        const response = JSON.parse(xhr.responseText);
                        if (response.disponible) {
                            status.textContent = 'Correo electrónico disponible.';
                            status.className = 'availability-available';
                        } else {
                            status.textContent = 'Correo electrónico no disponible.';
                            status.className = 'availability-unavailable';
                        }
                    } catch (e) {
                        status.textContent = 'Error al verificar disponibilidad.';
                        status.className = 'availability-unavailable';
                    }
                }
            };
            xhr.send();
        }
        
        function validateEmail(email) {
            const re = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
            return re.test(String(email).toLowerCase());
        }
		
    </script>
</body>
</html>