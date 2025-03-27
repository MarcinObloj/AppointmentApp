export interface User {
  id: number;
  firstName: string;
  lastName: string;
  email: string;
  password: string; 
  photoUrl: string;
  role: 'CLIENT' | 'EXPERT' | 'ADMIN'; 
  createdAt: string; 
  updatedAt: string;
  verified: boolean;
}
