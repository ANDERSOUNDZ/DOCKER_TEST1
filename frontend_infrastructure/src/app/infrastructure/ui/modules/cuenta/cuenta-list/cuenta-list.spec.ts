import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CuentaList } from './cuenta-list';

describe('CuentaList', () => {
  let component: CuentaList;
  let fixture: ComponentFixture<CuentaList>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [CuentaList]
    })
    .compileComponents();

    fixture = TestBed.createComponent(CuentaList);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
