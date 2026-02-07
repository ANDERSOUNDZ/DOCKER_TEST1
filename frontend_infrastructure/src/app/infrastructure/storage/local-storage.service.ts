import { Injectable } from '@angular/core';
import { StorageRepository } from '../../domain/repository/storage/storage.repository';

@Injectable({ providedIn: 'root' })
export class LocalStorageService implements StorageRepository {
  save(key: string, value: any): void {
    localStorage.setItem(key, JSON.stringify(value));
  }

  get<T>(key: string): T | null {
    const item = localStorage.getItem(key);
    return item ? (JSON.parse(item) as T) : null;
  }

  remove(key: string): void {
    localStorage.removeItem(key);
  }

  clear(): void {
    localStorage.clear();
  }
}
