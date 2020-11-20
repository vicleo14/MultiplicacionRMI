import java.rmi.Naming;

public class ServidorRMI
{
  public static void main(String[] args) throws Exception
  {
    String url = "rmi://localhost/multiplicacion";
    MultiplicacionRMI obj = new MultiplicacionRMI();

    // registra la instancia en el rmiregistry
    Naming.rebind(url,obj);
  }
}