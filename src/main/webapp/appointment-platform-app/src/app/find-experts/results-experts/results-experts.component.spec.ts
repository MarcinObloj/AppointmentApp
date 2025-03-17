import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ResultsExpertsComponent } from './results-experts.component';

describe('ResultsExpertsComponent', () => {
  let component: ResultsExpertsComponent;
  let fixture: ComponentFixture<ResultsExpertsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ResultsExpertsComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ResultsExpertsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
