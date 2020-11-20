import java.rmi.Naming;

public class ClienteRMI
{
  static int N = 4;

  public static void imprimirMatriz(int[][] matriz)
    {
        for(int i=0; i<N; i++)
        {
            for(int j=0; j<N; j++)
            {
                System.out.print(matriz[i][j] + "\t");
            }
            System.out.println("");
        }
    }

    public static long checksum(int[][] matriz)
    {
        long checksum = 0;
        for(int i=0; i< N; i++)
            for(int j=0;j<N; j++)
            checksum += matriz[i][j];

        return checksum;
    }

    static int[][] parte_matriz(int[][] A,int inicio)
    {
        int[][] M = new int[N/2][N];
        for (int i = 0; i < N/2; i++)
            for (int j = 0; j < N; j++)
            M[i][j] = A[i + inicio][j];
        return M;
    }

    static void acomoda_matriz(int[][] C,int[][] A,int renglon,int columna)
    {
    for (int i = 0; i < N/2; i++)
        for (int j = 0; j < N/2; j++)
        C[i + renglon][j + columna] = A[i][j];
    }
    public static void main(String args[]) throws Exception
    {
        String ip1 = args[0];
        String ip2 = args[1];
        String ip3 = args[2];
        String ip4 = args[3]; 
        // en este caso el objeto remoto se llama "prueba", notar que se utiliza el puerto default 1099
        String url0 = "rmi://" + args[0] + "/multiplicacion";
        String url1 = "rmi://" + args[1] + "/multiplicacion";
        String url2 = "rmi://" + args[2] + "/multiplicacion";
        String url3 = "rmi://" + args[3] + "/multiplicacion";
        int[][] A = new int[N][N];
        int[][] B = new int[N][N];

        /*
        Inicializamos la matriz
         */
        for (int i = 0; i< N; i++)
        {
            for(int j = 0; j< N; j++)
            {
                A[i][j]=2 * i - j;
                B[i][j] = 2 * i + j;
            }
        }

        /*
        Transponemos la matriz B
        */
        for (int i = 0; i < N; i++)
        for (int j = 0; j < i; j++)
        {
            int x = B[i][j];
            B[i][j] = B[j][i];
            B[j][i] = x;
        }

        /*
        Dividimos las matrices para ser multiplicadas
        */
        int[][] A1 = parte_matriz(A,0);
        int[][] A2 = parte_matriz(A,N/2);
        int[][] B1 = parte_matriz(B,0);
        int[][] B2 = parte_matriz(B,N/2);

        
        // obtiene una referencia que "apunta" al objeto remoto asociado a la URL
        IMultiplicacionRMI nodo0 = (IMultiplicacionRMI)Naming.lookup(url0);
        IMultiplicacionRMI nodo1 = (IMultiplicacionRMI)Naming.lookup(url1);
        IMultiplicacionRMI nodo2 = (IMultiplicacionRMI)Naming.lookup(url2);
        IMultiplicacionRMI nodo3 = (IMultiplicacionRMI)Naming.lookup(url3);


        /*
        Multiplicamos las submatrices con objetos remotos
        */
        int[][] C1 = nodo0.multiplica_matrices(A1,B1);
        int[][] C2 = nodo1.multiplica_matrices(A1,B2);
        int[][] C3 = nodo2.multiplica_matrices(A2,B1);
        int[][] C4 = nodo3.multiplica_matrices(A2,B2);

        /*
        Acomodamos los resultados en la matric C
        */

        int[][] C = new int[N][N];
        acomoda_matriz(C,C1,0,0);
        acomoda_matriz(C,C2,0,N/2);
        acomoda_matriz(C,C3,N/2,0);
        acomoda_matriz(C,C4,N/2,N/2);

        if(N==4)
        {
            System.out.println("*******   MATRIZ C  *******");
            imprimirMatriz(C);
        }
            
        System.out.println("El checksum de la matriz C es de: " + checksum(C));
    }
}