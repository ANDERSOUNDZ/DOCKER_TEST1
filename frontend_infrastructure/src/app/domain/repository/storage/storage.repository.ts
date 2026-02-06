export abstract class StorageRepository {
  abstract save(key: string, value: any): void;
  abstract get<T>(key: string): T | null;
  abstract remove(key: string): void;
  abstract clear(): void;
}
