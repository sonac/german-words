export interface CoreState {
  isLoading: boolean;
  error: string | null;
  word: Word | null;
}

export interface Word {
  english: string;
  german: string;
  article: string;
}
