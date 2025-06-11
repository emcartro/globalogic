package com.globallogic.app.users_app.consts;

import java.util.ArrayList;
import java.util.List;

/**
 * Clase encargada de contener las constantes del sistema.
 * @author </br>
 * Developer: Emilio Carcamo
 */
public class ConstantesApp {
	
	private ConstantesApp() {
		
	}
	

	public static final String AUTHORIZATION="Authorization";
	public static final String ACCEPT_LENGUAJE = "Accept-Language";
	public static final String API_APP="/api/users";


	public static final String ENCODING_UTF8 = "UTF-8";
	public static final String CONTENT_TYPE_JSON = "application/json";
	public static final String CONTENT_TYPE_JSON_UTF8 = "application/json;charset="+ENCODING_UTF8;
	public static final String TIMEZONE_UTC = "UTC";
	public static final String SWAGGER_PATH = "swagger-ui";
	public static final String SWAGGER_PATH_V3 = "v1/api-docs";
	public static final String TOKEN_TYPE = "token_type";
	public static final String TOKEN_ACCESS = "token_access";
	public static final String TOKEN_REFRESH_ATTEMPTS = "token_refresh_attemps";
	public static final String PATTERN_CORREO="^[A-Za-z0-9+_.-]+@(.+)$";
	public static final String PATTERN_PASSWORD = "^(?=.*[a-z])(?=(?:[^A-Z]*[A-Z]){1}(?![A-Z]))(?=(?:[^0-9]*[0-9]){2}(?![0-9]))[a-zA-Z0-9]{8,12}$";
	public  static final List<String> PUBLIC_ROUTES_SWAGGER = List.of(API_APP+"/"+SWAGGER_PATH,API_APP+"/"+SWAGGER_PATH_V3);
	public static final String AUTH_ROUTE=API_APP+"/auth";
	public static final String PUBLIC_RESOURCES="public-resource";
	public static final List<String> ENDPOINTS_PERMITALL= List.of("/auth","/user/request-reset-password/**","/user/reset-password/**/","/"+PUBLIC_RESOURCES+"/**");
	
	public  static final String ROUTE_REFRESH_TOKEN =API_APP+"/refresh-token";
	

}
