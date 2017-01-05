package transformada;

public class a {

	public static void main(String[] args) {
		
        String appPackageName="hookahPlace";
		String link= "\"https://play.google.com/store/apps/details?id=" + appPackageName+"\"";
        String message = "<a href="+link+"> Hookah Place </a>";
        System.out.println(message);
	}
}
