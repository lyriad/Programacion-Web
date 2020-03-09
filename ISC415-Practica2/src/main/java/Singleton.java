import Models.Estudiante;

import java.util.ArrayList;
import java.util.List;

public class Singleton {

    private List<Estudiante> estudiantes;
    private static Singleton singleton;

    private Singleton() {
        this.estudiantes = new ArrayList<>();
    }

    public static Singleton getInstance () {
        if (singleton == null) {
            singleton = new Singleton();
        }
        return singleton;
    }

    public List<Estudiante> getEstudiantes() {
        return estudiantes;
    }

    public Estudiante getEstudiante (int matricula) {

        Estudiante estudiante = null;
        int index = 0;

        while (index < estudiantes.size()) {
            if (estudiantes.get(index).getMatricula() == matricula) {
                estudiante = estudiantes.get(index);
            }
            index++;
        }

        return estudiante;
    }

    public boolean insertEstudiante (Estudiante estudiante) {
        if (estudiante != null) {
            estudiantes.add(estudiante);
        }
        return false;
    }

    public boolean deleteEstudiante (int matricula) {

        return estudiantes.remove(getEstudiante(matricula));
    }

    public boolean updateEstudiante (Estudiante estudiante) {

        int index = 0;

        while (index < estudiantes.size()) {
            if (estudiantes.get(index).getMatricula() == estudiante.getMatricula()) {
                estudiantes.set(index, estudiante);
                return true;
            }
            index++;
        }

        return false;
    }
}
