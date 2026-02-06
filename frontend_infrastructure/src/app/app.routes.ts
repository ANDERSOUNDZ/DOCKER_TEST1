import { Routes } from '@angular/router';
import { Layout } from './infrastructure/ui/shared/layout/layout';
import { ClienteList } from './infrastructure/ui/modules/cliente/cliente-list/cliente-list';
import { CuentaList } from './infrastructure/ui/modules/cuenta/cuenta-list/cuenta-list';
import { MovimientoList } from './infrastructure/ui/modules/movimiento/movimiento-list/movimiento-list';
import { ReporteList } from './infrastructure/ui/modules/reporte/reporte-list/reporte-list';

export const routes: Routes = [
  {
    path: '',
    component: Layout,
    children: [
      { path: 'clientes', component: ClienteList },
      { path: 'cuentas', component: CuentaList },
      { path: 'movimientos', component: MovimientoList },
      { path: 'reportes', component: ReporteList },
      { path: '', redirectTo: 'clientes', pathMatch: 'full' }
    ]
  }
];
