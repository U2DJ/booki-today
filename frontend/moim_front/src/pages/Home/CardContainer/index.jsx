import { useEffect } from "react";
import people from "../../../assets/svg/personIcon.svg";
import { useNavigate } from "react-router";
function CardContainer({
  image,
  category,
  title,
  capacity,
  currentCount,
  moimId,
}) {
  const navigate = useNavigate();

  return (
    <div
      className=" hover:cursor-pointer w-[360px] "
      onClick={() => navigate(`/detailed/${moimId}`)}
    >
      <img
        src={image}
        alt="cardContainer"
        className="w-full h-[100px] object-cover rounded-t-3xl"
      />
      <div className="bg-white w-auto h-auto drop-shadow rounded-b-3xl flex flex-col gap-5 py-3 px-12 ">
        <div className="flex flex-col gap-5">
          <div className="flex flex-col flex-wrap content-center">
            <div className=" font-Pretendard_Normal text-sm text-[#A1A3A5]">
              {category}
            </div>
            <div className=" font-Pretendard_SemiBold text-xl">{title}</div>
            <div className="flex font-Pretendard_SemiBold text-sm text-[#6F6F6F] pt-5 gap-2">
              <img src={people} />
              <div className="flex">
                <div>{currentCount}</div>
                <div>/</div>
                <div>{capacity}</div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
}
export default CardContainer;