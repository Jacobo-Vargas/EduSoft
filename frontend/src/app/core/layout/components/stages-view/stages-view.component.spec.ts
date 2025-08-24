import { ComponentFixture, TestBed } from '@angular/core/testing';

import { StagesViewComponent } from './stages-view.component';

describe('StagesViewComponent', () => {
  let component: StagesViewComponent;
  let fixture: ComponentFixture<StagesViewComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [StagesViewComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(StagesViewComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
