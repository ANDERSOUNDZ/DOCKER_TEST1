import { HttpInterceptorFn } from '@angular/common/http';
import { inject } from '@angular/core';
import { catchError, throwError } from 'rxjs';
import { Alert } from '../../services/alert/alert';

export const errorInterceptor: HttpInterceptorFn = (req, next) => {
  const notify = inject(Alert);

  return next(req).pipe(
    catchError((err) => {
      const errorMessage = err.error?.message || 'Ocurrió un error inesperado';
      notify.show(errorMessage, 'error');
      return throwError(() => err);
    }),
  );
};
