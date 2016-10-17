package ve.smile.web.viewmodels.calendar;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import karen.core.util.payload.UtilPayload;

import org.zkoss.calendar.api.CalendarEvent;

import ve.smile.consume.services.S;
import ve.smile.dto.EventoPlanificado;
import ve.smile.enums.EstatusEventoPlanificadoEnum;
import ve.smile.payload.response.PayloadEventoPlanificadoResponse;


public class DemoCalendarData {

	private List<CalendarEvent> calendarEvents = new LinkedList<CalendarEvent>();
	private final SimpleDateFormat DATA_FORMAT = new SimpleDateFormat("yyyy/MM/dd HH:mm");
	private Calendar cal = Calendar.getInstance();
	private List<EventoPlanificado> eventosPlan;
	private Date longDate;



	public List<EventoPlanificado> getEventosPlan() {
		
		int year = cal.get(Calendar.YEAR);
		String diaUnoString = year + "/01/01 00:00";
		Date diaUnoDate = getDate(diaUnoString);
		Long diaUnoLong = diaUnoDate.getTime();
		
		if (this.eventosPlan == null) {
			this.eventosPlan = new ArrayList<>();
		}
		this.eventosPlan.clear();
		if (this.eventosPlan.isEmpty()) {

			PayloadEventoPlanificadoResponse payloadEventoPlanificadoResponse = S.EventoPlanificadoService
					.consultaEventoPlanificadoPublicable(true, 
							EstatusEventoPlanificadoEnum.PLANIFICADO.ordinal(),
							diaUnoLong, 100);

			if (UtilPayload.isOK(payloadEventoPlanificadoResponse)) {
				this.eventosPlan.addAll(payloadEventoPlanificadoResponse
						.getObjetos());
			}

		}
		
		return eventosPlan;
	}

	public DemoCalendarData() {
		init();
	}

	private void init() {
//		int mod = cal.get(Calendar.MONTH) + 1;
//		int year = cal.get(Calendar.YEAR);
//		String date2 = mod > 9 ? year + "/" + mod + "" : year + "/" + "0" + mod;
//		calendarEvents.add(new DemoCalendarEvent(getDate(date2 + "/28 20:00"), getDate(date2 + "/29 20:00"), "blue", "red", "11"));
		
		for(Iterator<EventoPlanificado> i = getEventosPlan().iterator(); i.hasNext(); ) {
		    EventoPlanificado item = i.next();
			calendarEvents.add(new DemoCalendarEvent(
					getDate(getdateFormat(getLongDate(item.getFechaPlanificada()-86400000))),
					getDate(getdateFormat(getLongDate(item.getFechaPlanificada()))),
					item.getFkEvento().getFkClasificadorEvento().getColor(),
					item.getFkEvento().getFkClasificadorEvento().getColor(),
					item.getFkEvento().getNombre(),
					item.getIdEventoPlanificado().toString()));
			
		}
	}

	private Date getDate(String dateText) {
		try {
			return DATA_FORMAT.parse(dateText);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}

	public List<CalendarEvent> getCalendarEvents() {
		return calendarEvents;
	}
	
	public String getdateFormat(Date date) {
		String dateFormat = new SimpleDateFormat("yyyy/MM/dd 20:00:00").format(date);
		return dateFormat;
	}
	
	public Date getLongDate(Long date) {
		this.longDate = new Date(date);
		return longDate;
	}
	

}
