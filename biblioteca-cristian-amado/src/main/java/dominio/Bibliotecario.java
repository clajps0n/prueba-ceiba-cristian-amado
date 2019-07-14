package dominio;

import dominio.repositorio.RepositorioLibro;
import dominio.repositorio.RepositorioPrestamo;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.ZoneId;
import java.text.ParseException;
import dominio.excepcion.*;
import java.util.Date;

import javax.persistence.NoResultException;

public class Bibliotecario {

	public static final String EL_LIBRO_NO_SE_ENCUENTRA_DISPONIBLE = "El libro no se encuentra disponible";
	public static final String EL_LIBRO_NO_SE_ENCUENTRA_EN_EL_INVENTARIO = "El libro no se encuentra en el inventario";
	public static final String ISBN_INVALIDO = "ISBN inválido";
	public static final String ISBN_PALINDROMO = "Los libros palíndromos sólo se prestan en la biblioteca";
	
	public static final int UMBRAL_SUMA_ISBN = 30;
	public static final int DIAS_PARA_ENTREGA_15 = 15;
	public static final int DIAS_PARA_ENTREGA_16 = 16;

	private RepositorioLibro repositorioLibro;
	private RepositorioPrestamo repositorioPrestamo;

	public Bibliotecario(RepositorioLibro repositorioLibro, RepositorioPrestamo repositorioPrestamo) {
		this.repositorioLibro = repositorioLibro;
		this.repositorioPrestamo = repositorioPrestamo;

	}

	public void prestar(String isbn, String nombreUsuario) {
		if(null == isbn || isbn.length() <= 0) {
			throw new PrestamoException(ISBN_INVALIDO);
		}
		if(esPalindromo(isbn)) {
			throw new PrestamoException(ISBN_PALINDROMO);
		}
		if(esPrestado(isbn)) {
			throw new PrestamoException(EL_LIBRO_NO_SE_ENCUENTRA_DISPONIBLE);
		}
		
		repositorioPrestamo.agregar(construirPrestamo(isbn, nombreUsuario));
	}
	
	public Prestamo construirPrestamo(String isbn, String nombreUsuario) {
		return new Prestamo(obtenerFechaActual(),
							repositorioLibro.obtenerPorIsbn(isbn),
							obtenerDiaDeEntrega(isbn, LocalDate.now()),
							nombreUsuario);
	}
	
	public Date obtenerFechaActual() {
		return new Date();
	}

	public boolean esPrestado(String isbn) {
		try {
			Libro libro = repositorioLibro.obtenerPorIsbn(isbn);
			return (null != this.repositorioPrestamo.obtenerLibroPrestadoPorIsbn(libro.getIsbn()));
		}catch(NoResultException e) {
			throw new PrestamoException(EL_LIBRO_NO_SE_ENCUENTRA_EN_EL_INVENTARIO);
		}catch(NullPointerException e) {
			throw new PrestamoException(EL_LIBRO_NO_SE_ENCUENTRA_EN_EL_INVENTARIO);
		}
	}
	
	public boolean esPalindromo(String isbn) {
		StringBuilder sb = new StringBuilder(isbn);
		return(sb.toString().equalsIgnoreCase(sb.reverse().toString()));
	}
	
	public boolean isbnMayor30(String isbn) {
		int sum = 0;		
		for (int i = 0; i < isbn.length(); i++) {
            if (Character.isDigit(isbn.charAt(i))) {
                sum += Integer.parseInt(isbn.substring(i,i+1));
            }
        }
		return (sum > UMBRAL_SUMA_ISBN);
	}
	
	public Date obtenerDiaDeEntrega(String isbn, LocalDate fechaActual) {
		Date diaEntrega = null;
		if(isbnMayor30(isbn)) {
			if(fechaActual.plusDays(DIAS_PARA_ENTREGA_15).getDayOfWeek() == DayOfWeek.SUNDAY) {
				diaEntrega = Date.from(fechaActual.plusDays(DIAS_PARA_ENTREGA_16).atStartOfDay(ZoneId.systemDefault()).toInstant());
			}else {
				diaEntrega = Date.from(fechaActual.plusDays(DIAS_PARA_ENTREGA_15).atStartOfDay(ZoneId.systemDefault()).toInstant());
			}
		}
		return diaEntrega;
	}
}
