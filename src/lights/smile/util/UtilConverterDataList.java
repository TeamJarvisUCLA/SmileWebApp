package lights.smile.util;

import java.util.Calendar;

import ve.smile.seguridad.dto.Sesion;

public class UtilConverterDataList {

	private static long MULT_SEGUNDOS = 1000;
	private static long MULT_MINUTOS = 60 * MULT_SEGUNDOS;
	private static long MULT_HORAS = 60 * MULT_MINUTOS;
	private static long MULT_DIAS = 24 * MULT_HORAS;
	
	public static String charToStringUsuarioEstatus(String estatus) {
		return (estatus.equals("A")?"Habilitado":"Inhabilitado");
	}
	
	public static String getAuditoriaIn0(Sesion sesion) {
		if (sesion.getAuditorias().size() == 0) {
			return "-";
		}
		
		return longToStringSesionTimeAgo(sesion.getAuditorias().get(0).getFecha(), Calendar.getInstance());
	}
	
	public static String sesionToStringSesionTimeConexionTotal(Sesion sesion) {
		Long difference = sesion.getFechaFin() - sesion.getFechaInicio();
		
		if (difference == 0) {
			if (sesion.getEstado().equals(Sesion.ACTIVO)) {
				return "Active";
			}
			
			return "ERROR";
		}
		
		Calendar toCompare = Calendar.getInstance();
		toCompare.setTimeInMillis(difference);
		
		return longToStringSesionTimeAgo(0L, toCompare);
	}
	
	public static String longToStringSesionTimeAgoCompareWithToday(Long fechaInicio) {
		return longToStringSesionTimeAgo(fechaInicio, Calendar.getInstance());
	}
	
	public static String longToStringSesionTimeAgo(Long fechaInicio, Calendar toCompare) {
		Calendar fechaActual = toCompare;
		
		long diferencia = fechaActual.getTimeInMillis() - fechaInicio;
		
		if (diferencia < 0) {
			return "-";
		}
		
		Long d = UtilConverterDataList.daysBetween(diferencia);
		String dias = (d > 0)?(d + " Dias,"):"";
		diferencia = diferencia - daysBetween(diferencia) * MULT_DIAS;
		
		Long h = hoursBetween(diferencia);
		String horas = (h > 0)?(h + " Horas,"):"";
		diferencia = diferencia - hoursBetween(diferencia) * MULT_HORAS;
		
		Long m = minutesBetween(diferencia);
		String minutos = (m > 0)?(m + " Minutos,"):"";
		diferencia = diferencia - minutesBetween(diferencia) * MULT_MINUTOS;
		
		String segundos = secondsBetween(diferencia) + " Segundos";
		return (dias + " " + horas + " " + minutos + " " + segundos).trim();
	}
	
	private static long daysBetween(Long diferencia) {
        return (long)(diferencia/ MULT_DIAS);
    }
	
	private static long hoursBetween(Long diferencia) {
        return (long)(diferencia / MULT_HORAS);
    }
	
	private static long minutesBetween(Long diferencia) {
        return (long)(diferencia / MULT_MINUTOS);
    }
	
	private static long secondsBetween(Long diferencia) {
        return (long)(diferencia / MULT_SEGUNDOS);
    }
}
