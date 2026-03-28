interface CommentBoxProps {
  value: string;
  onChange: (value: string) => void;
  placeholder?: string;
}

export default function CommentBox({ value, onChange, placeholder }: CommentBoxProps) {
  return (
    <textarea
      className="field min-h-28"
      value={value}
      onChange={(event) => onChange(event.target.value)}
      placeholder={placeholder ?? "Add comments"}
    />
  );
}
