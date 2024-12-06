import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;


public class NA_1240540_1240688 {

    public static void main(String[] args) throws FileNotFoundException {
        File file = new File("teste.txt");
        Scanner scFile = new Scanner(file);

        Scanner sc = new Scanner(System.in);
        final int DIA_A_TESTAR_PREVENCAO = 8;
        final double CUSTO_UNITARIO_RECARREGAR_BATERIA = 5.5;
        final int CARGA_COMPLETA = 100;
        final String MENSAGEM_PLANEAMENTO = "a) planeamento (km/dia/veículo) ";
        final String MENSAGEM_RECARGAS_BATERIA = "c) recargas das baterias ";

        //a-> 1240540
        int[][] planeamento = criarPlaneamento(scFile);
        imprimirMatriz(planeamento, MENSAGEM_PLANEAMENTO);
        scFile.close();

        //b-> 1240688
        int[] arrayKmTotal = determinarTotalKmPercorridos(planeamento);
        imprimirMatrizTotalKmPercorridos(arrayKmTotal);

        //c-> 1240540
        int[][] recargasDia = calcularRecargasDia(planeamento, CARGA_COMPLETA);
        imprimirMatriz(recargasDia, MENSAGEM_RECARGAS_BATERIA);

        //d-> 1240688
        double[][] arrayBateriaFinal = determinarPercentagemBateriaFinalDia(planeamento, CARGA_COMPLETA);
        imprimirMatrizDouble(arrayBateriaFinal);

        //e-> 1240540
        double[] mediaKilometrosFrotaPorDia = new double[planeamento[0].length];
        calcularMediaKilometrosPorDiaFrota(planeamento, mediaKilometrosFrotaPorDia);
        imprimirMatrizKm(mediaKilometrosFrotaPorDia);

        //f-> 1240688
        boolean[] arrayVeiculosAcimaMediaDiaria = obterArrayVeiculosDeslocacoesAcimaMedia(planeamento, mediaKilometrosFrotaPorDia);
        imprimirVeiculosDeslocacoesAcimaMedia (arrayVeiculosAcimaMediaDiaria);

        //g-> 1240540
        int [] maxRecargasSeguidasPorCarro = new int[planeamento.length];
        int maxTotalRecargasSeguidas = obterMaxRecargasSeguidas(recargasDia, maxRecargasSeguidasPorCarro);
        imprimirVeiculosComMaximasRecargasSeguidas(maxRecargasSeguidasPorCarro, maxTotalRecargasSeguidas);

        //h-> 1240688
        System.out.printf("h) dia mais tardio em que todos os veículos necessitam de recarregar <%d> %n%n", obterDiaMaisTardioVeiculosRecarregamTodos(recargasDia));

        //i-> 1240540
        System.out.printf("i) custo das recargas da frota <%.2f €> %n%n",calcularCustoDasRecargas(recargasDia, CUSTO_UNITARIO_RECARREGAR_BATERIA));

        //j-> 1240688
        System.out.printf("j) veículo de prevenção no dia <%d> : V%d%n", DIA_A_TESTAR_PREVENCAO, obterVeiculoPrevencao(planeamento, arrayBateriaFinal, DIA_A_TESTAR_PREVENCAO));
    }

    private static int[][] criarPlaneamento(Scanner sc) {
        sc.nextLine();
        int carros = sc.nextInt();
        int dias = sc.nextInt();
        int[][] arr = new int[carros][dias];

        for (int carro = 0; carro < arr.length; carro++) {
            for (int dia = 0; dia < arr[carro].length; dia++) {
                arr[carro][dia] = sc.nextInt();
            }
        }
        return arr;
    }

    private static void imprimirMatriz(int[][] arr, String mensagem) {
        imprimirLayout(arr[0].length, mensagem);
        for (int carro = 0; carro < arr.length; carro++) {
            System.out.printf("V%-3d:", carro);
            for (int dia = 0; dia < arr[carro].length; dia++) {
                System.out.printf("%8d", arr[carro][dia]);
            }
            System.out.println();
        }
        System.out.println();
    }

    private static void imprimirLayout(int dias, String mensagem) {
        System.out.println(mensagem);
        System.out.print("dia :");
        for (int dia = 0; dia < dias; dia++)
            System.out.printf("%8d", dia);
        System.out.printf("%n----|-");
        for (int dia = 0; dia < dias; dia++)
            System.out.print("-------|");
        System.out.println();
    }

    //b

    private static int[] determinarTotalKmPercorridos(int[][] arrayInformacao) {
        int[] arrayKmTotal = new int[arrayInformacao.length];
        for (int veiculo = 0; veiculo < arrayInformacao.length; veiculo++) {
            for (int dia = 0; dia < arrayInformacao[veiculo].length; dia++) {
                arrayKmTotal[veiculo] += arrayInformacao[veiculo][dia];
            }
        }
        return arrayKmTotal;
    }

    private static void imprimirMatrizTotalKmPercorridos(int[] arrayKmTotal) {
        System.out.println("b) total de km a percorrer ");
        for (int carro = 0; carro < arrayKmTotal.length; carro++) {
            System.out.printf("V%-3d:", carro);
            System.out.printf("%8d km", arrayKmTotal[carro]);
            System.out.println();
        }
        System.out.println();
    }

    private static int[][] calcularRecargasDia(int[][] planeamento, int carga) {
        int[][] recargasDia = new int[planeamento.length][planeamento[0].length];
        int numeroRecargasDia;
        int kilometrosDisponiveisCarro;

        for (int carro = 0; carro < planeamento.length; carro++) {
            kilometrosDisponiveisCarro = carga;
            for (int dia = 0; dia < planeamento[carro].length; dia++) {
                numeroRecargasDia = 0;
                kilometrosDisponiveisCarro -= planeamento[carro][dia];
                while (kilometrosDisponiveisCarro <= 0) {
                    numeroRecargasDia++;
                    kilometrosDisponiveisCarro += carga;
                }
                recargasDia[carro][dia] = numeroRecargasDia;
            }
        }
        return recargasDia;
    }

    //d
    private static double[][] determinarPercentagemBateriaFinalDia(int[][] arrayInformacao, int carga) {
        double[][] arrayBateriaFinal = new double[arrayInformacao.length][arrayInformacao[0].length];
        for (int veiculo = 0; veiculo < arrayInformacao.length; veiculo++) {
            double bateriaInicial = carga;
            for (int dia = 0; dia < arrayInformacao[veiculo].length; dia++) {
                double percentagemFinalBateria = bateriaInicial - (double) arrayInformacao[veiculo][dia];
                while (percentagemFinalBateria <= 0) {
                    percentagemFinalBateria += carga;
                }
                arrayBateriaFinal[veiculo][dia] = percentagemFinalBateria;
                bateriaInicial = percentagemFinalBateria;
            }
        }
        return arrayBateriaFinal;
    }

    private static void imprimirMatrizDouble(double[][] arr) {
        imprimirLayout(arr[0].length, "d) carga das baterias ");
        for (int carro = 0; carro < arr.length; carro++) {
            System.out.printf("V%-3d:", carro);
            for (int dia = 0; dia < arr[carro].length; dia++) {
                System.out.printf("%7.1f%%", arr[carro][dia]);
            }
            System.out.println();
        }
        System.out.println();
    }

    //e
    private static void calcularMediaKilometrosPorDiaFrota(int[][] planeamento, double[] mediaKilometrosFrotaPorDia) {
        int totalKilometrosDia;
        for (int dia = 0; dia < planeamento[0].length; dia++) {
            totalKilometrosDia = 0;
            for (int carro = 0; carro < planeamento.length; carro++) {
                totalKilometrosDia += planeamento[carro][dia];
            }

            mediaKilometrosFrotaPorDia[dia] = ((double) totalKilometrosDia / planeamento.length);
        }
    }

    private static void imprimirMatrizKm(double[] arr) {
        imprimirLayout(arr.length, "e) média de km diários da frota");
        System.out.print("km  :");
        for (int dia = 0; dia < arr.length; dia++)
            System.out.printf("%8.1f", arr[dia]);
        System.out.println();

        System.out.println();
    }

    //f
    private static boolean [] obterArrayVeiculosDeslocacoesAcimaMedia(int[][] arrayInformacao, double[] arrayMediaDiaria) {
        boolean[] arrayVeiculosAcimaMediaDiaria = new boolean[arrayInformacao.length];
        for (int veiculo = 0; veiculo < arrayInformacao.length; veiculo++) {
            boolean sempreAcimaMediaDiaria = true;
            for (int dia = 0; dia < arrayInformacao[veiculo].length && sempreAcimaMediaDiaria; dia++) {
                if (arrayInformacao[veiculo][dia] <= arrayMediaDiaria[dia]) {
                    sempreAcimaMediaDiaria = false;
                }
            }
            if (sempreAcimaMediaDiaria) {
                arrayVeiculosAcimaMediaDiaria[veiculo] = true;
            }
        }
        return arrayVeiculosAcimaMediaDiaria;
    }

    private static int contarVeiculosAcimaMediaDiaria(boolean[] arrayVeiculosAcimaMediaDiaria) {
        int contadorVeiculosAcimaMedia = 0;
        for (int veiculo = 0; veiculo < arrayVeiculosAcimaMediaDiaria.length; veiculo++) {
            if (arrayVeiculosAcimaMediaDiaria[veiculo]) {
                contadorVeiculosAcimaMedia++;
            }
        }
        return contadorVeiculosAcimaMedia;
    }

    private static void imprimirIndividualmenteVeiculosAcimaMedia (boolean[] arrayVeiculosAcimaMediaDiaria) {
        for (int veiculo = 0; veiculo < arrayVeiculosAcimaMediaDiaria.length; veiculo++) {
            if (arrayVeiculosAcimaMediaDiaria[veiculo]) {
                System.out.printf("[V%d] ", veiculo);
            }
        }
        System.out.println();
    }

    private static void imprimirVeiculosDeslocacoesAcimaMedia (boolean[] arrayVeiculosAcimaMediaDiaria) {
        System.out.print("f) deslocações sempre acima da média diária");
        System.out.println();
        System.out.printf("<%d> veículos : ", contarVeiculosAcimaMediaDiaria(arrayVeiculosAcimaMediaDiaria));
        imprimirIndividualmenteVeiculosAcimaMedia(arrayVeiculosAcimaMediaDiaria);
        System.out.println();
    }

    //g
    private static int obterMaxRecargasSeguidas(int[][] arr, int [] recargasVeiculo) {
        int numeroRecargasSeguidas;
        int maximoRecargasSeguidas;
        int maxTotalRecargasSeguidas = 0;

        for (int carro = 0; carro < arr.length; carro++) {
            numeroRecargasSeguidas = 0;
            maximoRecargasSeguidas = 0;
            for (int dia = 0; dia < arr[0].length; dia++) {
                if (arr[carro][dia] > 0) {
                    numeroRecargasSeguidas++;
                } else {
                    numeroRecargasSeguidas = 0;

                }
                if (numeroRecargasSeguidas > maximoRecargasSeguidas)
                    maximoRecargasSeguidas = numeroRecargasSeguidas;
            }
            recargasVeiculo[carro] = maximoRecargasSeguidas;

            if (maximoRecargasSeguidas >= maxTotalRecargasSeguidas) {
                maxTotalRecargasSeguidas = maximoRecargasSeguidas;
            }
        }
        return maxTotalRecargasSeguidas;
    }

    private static void imprimirVeiculosComMaximasRecargasSeguidas(int[] arr, int max) {
        System.out.printf("g) veículos com mais dias consecutivas a necessitar de recarga%n");
        System.out.printf("<%d> dias consecutivos, veículos :", max);
        for (int carro = 0; carro < arr.length; carro++) {
            if (arr[carro] == max)
                System.out.printf(" [V%d]", carro);
        }
        System.out.println();
        System.out.println();
    }

    //h
    private static int obterDiaMaisTardioVeiculosRecarregamTodos(int[][] arrayNumeroRecargas) {
        int diaMaisTardioRecarga = -1;
        for (int dia = 0; dia < arrayNumeroRecargas[0].length; dia++) {
            boolean todosVeiculosRecarregaram = true;
            for (int veiculo = 0; veiculo < arrayNumeroRecargas.length && todosVeiculosRecarregaram; veiculo++) {
                if (arrayNumeroRecargas[veiculo][dia] == 0) {
                    todosVeiculosRecarregaram = false;
                }
            }
            if (todosVeiculosRecarregaram) {
                diaMaisTardioRecarga = dia;
            }

        }
        return diaMaisTardioRecarga;

    }

    //i
    private static double calcularCustoDasRecargas(int[][] arr, double custoUnitario) {
        int numeroTotalRecargas = 0;

        for (int carro = 0; carro < arr.length; carro++) {
            for (int dia = 0; dia < arr[carro].length; dia++) {
                numeroTotalRecargas += arr[carro][dia];
            }
        }
        return (numeroTotalRecargas * custoUnitario);
    }

    //j
    private static int obterVeiculoPrevencao(int[][] arrayInformacao, double[][] arrayBateriaFinal, int dia) {
        int veiculoPrevencao;
        veiculoPrevencao = 0;
        for (int veiculo = 0; veiculo < arrayInformacao.length; veiculo++) {
            if (arrayInformacao[veiculo][dia] < arrayInformacao[veiculoPrevencao][dia]) {
                veiculoPrevencao = veiculo;
            }
            if (arrayInformacao[veiculo][dia] == arrayInformacao[veiculoPrevencao][dia]) {
                if (arrayBateriaFinal[veiculo][dia] > arrayBateriaFinal[veiculoPrevencao][dia]) {
                    veiculoPrevencao = veiculo;
                }
            }
        }
        return veiculoPrevencao;
    }
}
