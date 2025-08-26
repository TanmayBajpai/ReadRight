import { Cell, Pie, PieChart } from "recharts";

const RADIAN = Math.PI / 180;

const chartData = [
    { name: "Left", value: 90},
    { name: "Right", value: 90},
];

const needle = ({ value, data, cx, cy, iR, oR, color }) => {
    const total = data.reduce((sum, entry) => sum + entry.value, 0);
    const ang = 180.0 * (1 - value / total);
    const length = (iR + 2 * oR) / 3;
    const sin = Math.sin(-RADIAN * ang);
    const cos = Math.cos(-RADIAN * ang);
    const r = 5;
    const x0 = cx + 5;
    const y0 = cy + 5;
    const xba = x0 + r * sin;
    const yba = y0 - r * cos;
    const xbb = x0 - r * sin;
    const ybb = y0 + r * cos;
    const xp = x0 + length * cos;
    const yp = y0 + length * sin;

    return [
        <circle
            key="needle-circle"
            cx={x0}
            cy={y0}
            r={r}
            fill={color}
            stroke="none"
        />,
        <path
            key="needle-path"
            d={`M${xba} ${yba}L${xbb} ${ybb} L${xp} ${yp} L${xba} ${yba}`}
            fill={color}
        />,
    ];
};

export default function Dial({ value = 0 }) {
    const cx = 121;
    const cy = 100;
    const iR = 80;
    const oR = 100;

    return (
        <PieChart width={250} height={110} style={{marginTop: "60px"}}>
            <defs>
                <linearGradient id="leftGradient" x1="0" y1="0" x2="1" y2="0">
                    <stop offset="0%" stopColor="#000986ff" />
                    <stop offset="100%" stopColor="#0011ffff" />
                    
                </linearGradient>

                <linearGradient id="rightGradient" x1="0" y1="0" x2="1" y2="0">
                    <stop offset="0%" stopColor="#d60000ff" />
                    <stop offset="100%" stopColor="#ae0000ff" />
                </linearGradient>
            </defs>
            <Pie
                dataKey="value"
                startAngle={180}
                endAngle={0}
                data={chartData}
                cx={cx}
                cy={cy}
                innerRadius={iR}
                outerRadius={oR}
                stroke="none"
            >
                <Cell fill="url(#leftGradient)" /> 
                <Cell fill="url(#rightGradient)" />
            </Pie>
            {needle({ value, data: chartData, cx, cy, iR, oR, color: "#000000ff" })}
        </PieChart>
    );
}