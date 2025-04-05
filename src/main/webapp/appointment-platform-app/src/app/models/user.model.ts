import { ExpertDTO } from "./answer.model";

export interface User {
  id: number;
  firstName: string;
  lastName: string;
  email: string;
  password: string; 
  photoUrl: string;
  role: 'ROLE_CLIENT' | 'ROLE_EXPERT' | 'ROLE_ADMIN'; 
  createdAt: string; 
  updatedAt: string;
  verified: boolean;
  expert?: {
    id: number;
    city: string;
    street: string;
  };
}
