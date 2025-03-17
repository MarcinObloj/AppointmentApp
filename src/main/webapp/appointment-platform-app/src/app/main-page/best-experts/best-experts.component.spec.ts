import { ComponentFixture, TestBed } from '@angular/core/testing';

import { BestExpertsComponent } from './best-experts.component';

describe('BestExpertsComponent', () => {
  let component: BestExpertsComponent;
  let fixture: ComponentFixture<BestExpertsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [BestExpertsComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(BestExpertsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
