import { Component, OnInit } from '@angular/core';
import { ApiService } from '../../services/api.service';

@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.scss']
})
export class DashboardComponent implements OnInit {
  stats = {
    totalSpecialites: 0,
    totalNiveaux: 0,
    totalClasses: 0,
    totalEleves: 0
  };

  constructor(private apiService: ApiService) {}

  ngOnInit(): void {
    // TODO: Implémenter le chargement des statistiques
  }
} 