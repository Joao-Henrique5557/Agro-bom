package br.com.agrobombackend.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import jakarta.servlet.http.HttpServletRequest;

@ExtendWith(MockitoExtension.class)
class RelatorioServletTest {

    @Mock
    private HttpServletRequest request;

    @Test
    void intParam_devolveValorDoParametroQuandoPresente() {
        when(request.getParameter("mes")).thenReturn("5");

        int resultado = RelatorioServlet.intParam(request, "mes", 1);

        assertEquals(5, resultado);
    }

    @Test
    void intParam_devolveDefaultQuandoParametroAusente() {
        when(request.getParameter("mes")).thenReturn(null);

        int resultado = RelatorioServlet.intParam(request, "mes", 8);

        assertEquals(8, resultado);
    }

    @Test
    void intParam_devolveDefaultQuandoParametroInvalido() {
        when(request.getParameter("mes")).thenReturn("nao-e-numero");

        int resultado = RelatorioServlet.intParam(request, "mes", 3);

        assertEquals(3, resultado);
    }
}
