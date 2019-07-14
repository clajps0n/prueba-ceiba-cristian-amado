package dominio.integracion;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import dominio.Bibliotecario;
import dominio.Libro;
import dominio.excepcion.PrestamoException;
import dominio.repositorio.RepositorioLibro;
import dominio.repositorio.RepositorioPrestamo;
import persistencia.sistema.SistemaDePersistencia;
import testdatabuilder.LibroTestDataBuilder;

public class BibliotecarioTest {

	private static final String CRONICA_DE_UNA_MUERTA_ANUNCIADA = "Cronica de una muerta anunciada";
	private static final String CANDELARIA = "Candelaria";
	
	private SistemaDePersistencia sistemaPersistencia;
	
	private RepositorioLibro repositorioLibros;
	private RepositorioPrestamo repositorioPrestamo;

	@Before
	public void setUp() {
		
		sistemaPersistencia = new SistemaDePersistencia();
		
		repositorioLibros = sistemaPersistencia.obtenerRepositorioLibros();
		repositorioPrestamo = sistemaPersistencia.obtenerRepositorioPrestamos();
		
		sistemaPersistencia.iniciar();
	}
	

	@After
	public void tearDown() {
		sistemaPersistencia.terminar();
	}

	@Test
	public void prestarCuandoCumpleLasCondicionesEntoncesLoPrestaTest() {
		// arrange
		Libro libro = new LibroTestDataBuilder().conTitulo(CRONICA_DE_UNA_MUERTA_ANUNCIADA).build();
		repositorioLibros.agregar(libro);
		Bibliotecario blibliotecario = new Bibliotecario(repositorioLibros, repositorioPrestamo);

		String nombreUsuario = "Jhon Snow";
		// act
		blibliotecario.prestar(libro.getIsbn(), nombreUsuario);

		// assert
		Assert.assertTrue(blibliotecario.esPrestado(libro.getIsbn()));
		Assert.assertNotNull(repositorioPrestamo.obtenerLibroPrestadoPorIsbn(libro.getIsbn()));

	}
	
	@Test
	public void prestarCuandoIsbnEsVacioEntoncesDisparaExcepcionTest() {
		// arrange
		Libro libro = new LibroTestDataBuilder().conIsbn("").conTitulo(CRONICA_DE_UNA_MUERTA_ANUNCIADA).build();
		
		repositorioLibros.agregar(libro);
		
		Bibliotecario blibliotecario = new Bibliotecario(repositorioLibros, repositorioPrestamo);

		String nombreUsuario = "Jhon Snow";
		
		try {
			blibliotecario.prestar(libro.getIsbn(), nombreUsuario);
			fail();
		} catch (PrestamoException e) {
			// assert
			Assert.assertEquals(Bibliotecario.ISBN_INVALIDO, e.getMessage());
		}
	}
	
	@Test
	public void prestarCuandoIsbnEsPalindromoEntoncesDisparaExcepcionTest() {
		// arrange
		Libro libro = new LibroTestDataBuilder().conIsbn("1221").conTitulo(CRONICA_DE_UNA_MUERTA_ANUNCIADA).build();
		
		repositorioLibros.agregar(libro);
		
		Bibliotecario blibliotecario = new Bibliotecario(repositorioLibros, repositorioPrestamo);

		String nombreUsuario = "Jhon Snow";
		
		try {
			blibliotecario.prestar(libro.getIsbn(), nombreUsuario);
			fail();
		} catch (PrestamoException e) {
			// assert
			Assert.assertEquals(Bibliotecario.ISBN_PALINDROMO, e.getMessage());
		}
	}

	@Test
	public void prestarCuandoElLibroNoEstaDisponibleEntoncesDisparaExcepcionTest() {
		// arrange
		Libro libro = new LibroTestDataBuilder().conTitulo(CRONICA_DE_UNA_MUERTA_ANUNCIADA).build();
		
		repositorioLibros.agregar(libro);
		
		Bibliotecario blibliotecario = new Bibliotecario(repositorioLibros, repositorioPrestamo);

		String nombreUsuario = "Jhon Snow";
		
		// act
		blibliotecario.prestar(libro.getIsbn(), nombreUsuario);
		
		try {
			blibliotecario.prestar(libro.getIsbn(), nombreUsuario);
			fail();
		} catch (PrestamoException e) {
			// assert
			Assert.assertEquals(Bibliotecario.EL_LIBRO_NO_SE_ENCUENTRA_DISPONIBLE, e.getMessage());
		}
	}

	@Test
	public void prestarCuandoElLibroNoExisteEntoncesDisparaExcepcionTest() {
		Libro libro = new LibroTestDataBuilder().conIsbn("ABCD").conTitulo(CRONICA_DE_UNA_MUERTA_ANUNCIADA).build();
		
		repositorioLibros.agregar(libro);
		
		Bibliotecario blibliotecario = new Bibliotecario(repositorioLibros, repositorioPrestamo);

		String nombreUsuario = "Jhon Snow";
		
		try {
			blibliotecario.prestar("XYZ", nombreUsuario);
			fail();
		} catch (PrestamoException e) {
			// assert
			Assert.assertEquals(Bibliotecario.EL_LIBRO_NO_SE_ENCUENTRA_EN_EL_INVENTARIO, e.getMessage());
		}
	}
	
	@Test
	public void prestarCuandoIsbnMenor30EntoncesMaximaEntregaVaciaTest() {
		// arrange
		Libro libro = new LibroTestDataBuilder().conIsbn("123").build();
		
		repositorioLibros.agregar(libro);
		
		Bibliotecario blibliotecario = new Bibliotecario(repositorioLibros, repositorioPrestamo);

		String nombreUsuario = "Jhon Snow";
		
		// act
		blibliotecario.prestar(libro.getIsbn(), nombreUsuario);
		
		// assert
		Assert.assertNull(repositorioPrestamo.obtener(libro.getIsbn()).getFechaEntregaMaxima());
	}
	
	@Test
	public void prestarCuandoIsbnMayor30EntoncesFechaMaximaEntregaNoVaciaTest() {
		// arrange
		Libro libro = new LibroTestDataBuilder().conIsbn("99991").build();
		
		repositorioLibros.agregar(libro);
		
		Bibliotecario blibliotecario = new Bibliotecario(repositorioLibros, repositorioPrestamo);

		String nombreUsuario = "Jhon Snow";
		
		// act
		blibliotecario.prestar(libro.getIsbn(), nombreUsuario);
		
		// assert
		Assert.assertNotNull(repositorioPrestamo.obtener(libro.getIsbn()).getFechaEntregaMaxima());
	}
}