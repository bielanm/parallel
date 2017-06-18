package com.bielanm.util;


import mpi.MPI;
import mpi.Status;

public class MPIUtil {

    public static double[] readPossibleDouble(int source) {
        Status status = MPI.COMM_WORLD.Probe(source, 0);
        int length = status.Get_count(MPI.DOUBLE);
        double[] buffer = new double[length];
        MPI.COMM_WORLD.Recv(buffer, 0, length, MPI.DOUBLE, source, 0);

        return buffer;
    };

    public static void sendDouble(int dest, double[] buffer) {
        MPI.COMM_WORLD.Send(buffer, 0, buffer.length, MPI.DOUBLE, dest, 0);
    }

    public static void sendInt(int dest, int[] buffer) {
        MPI.COMM_WORLD.Send(buffer, 0, buffer.length, MPI.INT, dest, 0);
    }

    public static int[] readPossibleInt(int source) {
        Status status = MPI.COMM_WORLD.Probe(source, 0);
        int length = status.Get_count(MPI.INT);
        int[] buffer = new int[length];
        MPI.COMM_WORLD.Recv(buffer, 0, length, MPI.INT, source, 0);

        return buffer;
    };
}
