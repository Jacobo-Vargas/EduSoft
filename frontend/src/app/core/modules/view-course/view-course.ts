import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { CourseService } from '../../services/course.service';
import { ModuleService, ModuleResponseDto } from '../../services/module.service';
import { LessonService, LessonResponseDto } from '../../services/lesson.service';
import { ContentService, ContentResponseDto } from '../../services/content.service';
import { forkJoin } from 'rxjs';

@Component({
  selector: 'app-view-course',
  standalone: false,
  templateUrl: './view-course.html',
  styleUrls: ['./view-course.css']
})
export class ViewCourseComponent implements OnInit {
  courseId!: number;
  course: any;
  modules: ModuleResponseDto[] = [];
  lessonsByModule: Record<number, LessonResponseDto[]> = {};
  contentsByLesson: Record<number, ContentResponseDto[]> = {};
  loading = true;

  constructor(
    private route: ActivatedRoute,
    private courseService: CourseService,
    private moduleService: ModuleService,
    private lessonService: LessonService,
    private contentService: ContentService
  ) { }

  ngOnInit(): void {
    this.courseId = +this.route.snapshot.paramMap.get('id')!;
    this.loadCourseData();
  }

  loadCourseData(): void {
    this.loading = true;

    this.courseService.getCourseById(this.courseId).subscribe({
      next: (course) => {
        this.course = course;
        this.loadModules();
      },
      error: (err) => {
        console.error('❌ Error al cargar curso:', err);
        this.loading = false;
      }
    });
  }

  loadModules(): void {
    this.moduleService.getModulesByCourse(this.courseId).subscribe({
      next: (modules) => {
        this.modules = modules;
        if (modules.length === 0) {
          this.loading = false;
          return;
        }

        const lessonRequests = modules.map(m => this.lessonService.getLessonsByModule(m.id));

        forkJoin(lessonRequests).subscribe({
          next: (lessonsArrays) => {
            lessonsArrays.forEach((lessons, index) => {
              const moduleId = this.modules[index].id;
              this.lessonsByModule[moduleId] = lessons;

              lessons.forEach(lesson => {
                this.contentService.getContentsByLesson(lesson.id).subscribe({
                  next: (contents) => this.contentsByLesson[lesson.id] = contents,
                  error: (err) => console.error('❌ Error al cargar contenidos:', err)
                });
              });
            });

            this.loading = false;
          },
          error: (err) => {
            console.error('❌ Error al cargar lecciones:', err);
            this.loading = false;
          }
        });
      },
      error: (err) => {
        console.error('❌ Error al cargar módulos:', err);
        this.loading = false;
      }
    });
  }

  getFileType(url: string): string {
    const ext = (url.split('.').pop() || '').toLowerCase();

    if (['jpg', 'jpeg', 'png', 'gif', 'webp'].includes(ext)) return 'image';
    if (['mp4', 'mkv', 'webm', 'mov', 'avi', 'ogg'].includes(ext)) return 'video';
    if (ext === 'pdf') return 'pdf';
    return 'other';
  }

}
