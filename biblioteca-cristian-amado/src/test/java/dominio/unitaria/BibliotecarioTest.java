package dominio.unitaria;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

import org.junit.Assert;
import org.junit.Test;

import dominio.Bibliotecario;
import dominio.Libro;
import dominio.excepcion.PrestamoException;
import dominio.repositorio.RepositorioLibro;
import dominio.repositorio.RepositorioPrestamo;
import testdatabuilder.LibroTestDataBuilder;

public class BibliotecarioTest {
	
	@Test
	public void esPrestadoCuandoElLibroEstaPrestadoEntoncesRetornaTrueTest() {
		Libro libro = new LibroTestDataBuilder().build();
		
		RepositorioLibro repositorioLibro = mock(RepositorioLibro.class);
		RepositorioPrestamo repositorioPrestamo = mock(RepositorioPrestamo.class);
		
		Bibliotecario bibliotecario = new Bibliotecario(repositorioLibro, repositorioPrestamo);
		
		when(repositorioLibro.obtenerPorIsbn(libro.getIsbn())).thenReturn(libro);
		when(repositorioPrestamo.obtenerLibroPrestadoPorIsbn(libro.getIsbn())).thenReturn(libro);
		
		//act
		boolean esPrestado = bibliotecario.esPrestado(libro.getIsbn());
		//assert
		assertTrue(esPrestado); 
	}
		
	@Test
	public void esPrestadoCuandoElLibroNoEstaPrestadoEntoncesRetornaFalseTest() {
		Libro libro = new LibroTestDataBuilder().build();
		
		RepositorioLibro repositorioLibro = mock(RepositorioLibro.class);
		RepositorioPrestamo repositorioPrestamo = mock(RepositorioPrestamo.class);
		
		Bibliotecario bibliotecario = new Bibliotecario(repositorioLibro, repositorioPrestamo);
		
		when(repositorioLibro.obtenerPorIsbn(libro.getIsbn())).thenReturn(libro);
		when(repositorioPrestamo.obtenerLibroPrestadoPorIsbn(libro.getIsbn())).thenReturn(null);
		
		//act
		boolean esPrestado = bibliotecario.esPrestado(libro.getIsbn());
		//assert
		assertFalse(esPrestado); 
	}
	
	@Test
	public void esPrestadoCuandoElLibroNoExisteEntoncesDisparaExcepcionTest() {
		Libro libro = new LibroTestDataBuilder().build();
		
		RepositorioLibro repositorioLibro = mock(RepositorioLibro.class);
		RepositorioPrestamo repositorioPrestamo = mock(RepositorioPrestamo.class);
		
		Bibliotecario bibliotecario = new Bibliotecario(repositorioLibro, repositorioPrestamo);
		
		//when(repositorioLibro.obtenerPorIsbn(libro.getIsbn())).thenReturn(null);
		when(repositorioPrestamo.obtenerLibroPrestadoPorIsbn(libro.getIsbn())).thenReturn(null);
				
		try {
			bibliotecario.esPrestado(libro.getIsbn());
			fail();
		} catch (PrestamoException e) {
			// assert
			Assert.assertEquals(Bibliotecario.EL_LIBRO_NO_SE_ENCUENTRA_EN_EL_INVENTARIO, e.getMessage());
		} 
	}
	
	@Test
	public void esPalindromoCuandoElIsbnNoContieneNumerosEntoncesRetornaTrueTest() {
		String isbn = "ABCCBA";
		
		RepositorioLibro repositorioLibro = mock(RepositorioLibro.class);
		RepositorioPrestamo repositorioPrestamo = mock(RepositorioPrestamo.class);
		
		Bibliotecario bibliotecario = new Bibliotecario(repositorioLibro, repositorioPrestamo);
		
		try {
			Assert.assertTrue(bibliotecario.esPalindromo(isbn));
		}catch(PrestamoException e) {
			fail();
		}
	}
	
	@Test
	public void esPalindromoCuandoElIsbnContieneNumerosEntoncesRetornaTrueTest() {
		String isbn = "ABC5CBA";
				
		RepositorioLibro repositorioLibro = mock(RepositorioLibro.class);
		RepositorioPrestamo repositorioPrestamo = mock(RepositorioPrestamo.class);
		
		Bibliotecario bibliotecario = new Bibliotecario(repositorioLibro, repositorioPrestamo);
		
		try {
			Assert.assertTrue(bibliotecario.esPalindromo(isbn));
		}catch(PrestamoException e) {
			fail();
		}
	}
	
	@Test
	public void esPalindromoCuandoElIsbnTieneMayusculasYMinusculasEntoncesRetornaTrueTest() {
		String isbn = "AaaaaAaaaAaa";
		
		RepositorioLibro repositorioLibro = mock(RepositorioLibro.class);
		RepositorioPrestamo repositorioPrestamo = mock(RepositorioPrestamo.class);
		
		Bibliotecario bibliotecario = new Bibliotecario(repositorioLibro, repositorioPrestamo);
		
		try {
			Assert.assertTrue(bibliotecario.esPalindromo(isbn));
		}catch(PrestamoException e) {
			fail();
		}
	}
	
	@Test
	public void esPalindromoCuandoNoEsPalindromoEntoncesRetornaFalseTest() {
		String isbn = "A1BCC1B";
		
		RepositorioLibro repositorioLibro = mock(RepositorioLibro.class);
		RepositorioPrestamo repositorioPrestamo = mock(RepositorioPrestamo.class);
		
		Bibliotecario bibliotecario = new Bibliotecario(repositorioLibro, repositorioPrestamo);
		
		try {
			Assert.assertFalse(bibliotecario.esPalindromo(isbn));
		}catch(PrestamoException e) {
			fail();
		}
	}
	
	@Test
	public void isbnMayor30CuandoIsbnTieneCaracteresYDigitosSumanMasDe30EntoncesRetornaTrueTest() {
		String isbn = "9a9b9c9d";
		
		RepositorioLibro repositorioLibro = mock(RepositorioLibro.class);
		RepositorioPrestamo repositorioPrestamo = mock(RepositorioPrestamo.class);
		
		Bibliotecario bibliotecario = new Bibliotecario(repositorioLibro, repositorioPrestamo);
		
		try {
			Assert.assertTrue(bibliotecario.isbnMayor30(isbn));
		}catch(PrestamoException e) {
			fail();
		}
	}
	
	@Test
	public void isbnMayor30CuandoIsbnNoTieneCaracteresYDigitosSumanMasDe30EntoncesRetornaTrueTest() {
		String isbn = "9999";
		
		RepositorioLibro repositorioLibro = mock(RepositorioLibro.class);
		RepositorioPrestamo repositorioPrestamo = mock(RepositorioPrestamo.class);
		
		Bibliotecario bibliotecario = new Bibliotecario(repositorioLibro, repositorioPrestamo);
		
		try {
			Assert.assertTrue(bibliotecario.isbnMayor30(isbn));
		}catch(PrestamoException e) {
			fail();
		}
	}
	
	@Test
	public void isbnMayor30CuandoDigitosSuman30EntoncesRetornaFalseTest() {
		String isbn = "9993";
		
		RepositorioLibro repositorioLibro = mock(RepositorioLibro.class);
		RepositorioPrestamo repositorioPrestamo = mock(RepositorioPrestamo.class);
		
		Bibliotecario bibliotecario = new Bibliotecario(repositorioLibro, repositorioPrestamo);
		
		try {
			Assert.assertFalse(bibliotecario.isbnMayor30(isbn));
		}catch(PrestamoException e) {
			fail();
		}
	}
	
	@Test
	public void isbnMayor30CuandoDigitosSumanMenosDe30EntoncesRetornaFalseTest() {
		String isbn = "999";
		
		RepositorioLibro repositorioLibro = mock(RepositorioLibro.class);
		RepositorioPrestamo repositorioPrestamo = mock(RepositorioPrestamo.class);
		
		Bibliotecario bibliotecario = new Bibliotecario(repositorioLibro, repositorioPrestamo);
		
		try {
			Assert.assertFalse(bibliotecario.isbnMayor30(isbn));
		}catch(PrestamoException e) {
			fail();
		}
	}
	
	@Test
	public void obtenerDiaEntregaCuandoNoCaeDomingoEntoncesRetornaEseDiaTest() {
		String isbn1 = "A6B7C8C9BA5";
		
		RepositorioLibro repositorioLibro = mock(RepositorioLibro.class);
		RepositorioPrestamo repositorioPrestamo = mock(RepositorioPrestamo.class);
		
		Bibliotecario bibliotecario = new Bibliotecario(repositorioLibro, repositorioPrestamo);
		
		try {
			Date fechaEsperada = Date.from(LocalDate.of(2019,7,16).atStartOfDay(ZoneId.systemDefault()).toInstant());
			Assert.assertEquals(fechaEsperada, bibliotecario.obtenerDiaDeEntrega(isbn1, LocalDate.of(2019, 7, 1)));
		}catch(IllegalArgumentException e) {
			fail();
		}
	}
	
	@Test
	public void obtenerDiaEntregaCuandoCaeDomingoEntoncesRetornaSiguienteDiaTest() {
		String isbn1 = "A6B7C8C9BA5";
		
		RepositorioLibro repositorioLibro = mock(RepositorioLibro.class);
		RepositorioPrestamo repositorioPrestamo = mock(RepositorioPrestamo.class);
		
		Bibliotecario bibliotecario = new Bibliotecario(repositorioLibro, repositorioPrestamo);
		
		try {
			Date fechaEsperada = Date.from(LocalDate.of(2019,7,22).atStartOfDay(ZoneId.systemDefault()).toInstant());
			Assert.assertEquals(fechaEsperada, bibliotecario.obtenerDiaDeEntrega(isbn1, LocalDate.of(2019, 7, 6)));
		}catch(IllegalArgumentException e) {
			fail();
		}
	}
	
	@Test
	public void prestarCuandoISBNesVacioEntoncesDisparaExcepcionTest() {
		Libro libro1 = new LibroTestDataBuilder().conTitulo("Candelaria")
												.conIsbn("")
												.build();
				
		RepositorioLibro repositorioLibro = mock(RepositorioLibro.class);
		RepositorioPrestamo repositorioPrestamo = mock(RepositorioPrestamo.class);
				
		Bibliotecario bibliotecario = new Bibliotecario(repositorioLibro, repositorioPrestamo);
				
		try {
			bibliotecario.prestar(libro1.getIsbn(), "Jonh Snow");
			fail();
		} catch (PrestamoException e) {
			// assert
			Assert.assertEquals(Bibliotecario.ISBN_INVALIDO, e.getMessage());
		} 
	}
	
	@Test
	public void prestarCuandoISBNesNuloEntoncesDisparaExcepcionTest() {
		Libro libro = new LibroTestDataBuilder().conTitulo("Candelaria")
												.conIsbn(null)
												.build();
		
		RepositorioLibro repositorioLibro = mock(RepositorioLibro.class);
		RepositorioPrestamo repositorioPrestamo = mock(RepositorioPrestamo.class);
				
		Bibliotecario bibliotecario = new Bibliotecario(repositorioLibro, repositorioPrestamo);
				
		try {
			bibliotecario.prestar(libro.getIsbn(), "Jonh Snow");
			fail();
		} catch (PrestamoException e) {
			// assert
			Assert.assertEquals(Bibliotecario.ISBN_INVALIDO, e.getMessage());
		}
	}
}
