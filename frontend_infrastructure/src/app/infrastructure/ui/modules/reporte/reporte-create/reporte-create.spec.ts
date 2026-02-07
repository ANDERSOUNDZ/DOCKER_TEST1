import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ReporteCreate } from './reporte-create';

describe('ReporteCreate', () => {
  let component: ReporteCreate;
  let fixture: ComponentFixture<ReporteCreate>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ReporteCreate]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ReporteCreate);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
