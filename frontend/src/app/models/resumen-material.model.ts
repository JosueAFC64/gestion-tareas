export interface RESummaryResponse {
  id: number;
  titulo: string;
  descripcion: string;
  tipo: string;
  url: string;
  fechaCreacion: string;
  creador: {
    nombres: string;
    apellidos: string;
  };
  curso: {
    nombre: string;
  };
}
